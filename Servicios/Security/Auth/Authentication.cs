using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Storage;
using Servicios.Models;
using Servicios.Models.Tables;
using Servicios.Security.Modules;
using Servicios.Security.Partial;

namespace Servicios.Security.Auth
{
    public class Authentication
    {
        RequestInterface request;
        EcoTrackContext DbContext;
        Firewall firewall;

        public Authentication(EcoTrackContext DbContext, RequestInterface request)
        {
            this.request = request;
            this.firewall = new Firewall();
            this.DbContext = DbContext;
        }

        public ResponseInterface Connect(Response response, bool JWE = false)
        {
            try
            {
                UsuarioLogin data = request.GetData<UsuarioLogin>(JWE);
                Usuario? usuario = this.DbContext.Usuario.Include(p => p.Persona).Include(p => p.TipoUsuario).Where(p => p.Cuenta == data.Usuario && p.Activo > 0).FirstOrDefault();

                if (usuario == null)
                {
                    return new ResponseInterface("Verifica que el usuario sea correcto", response.PartialContent, true);
                }

                string clave = Helper.CalculateMD5Hash(data.Contrasena);
                if (usuario.Contrasena.ToLower() != clave.ToLower())
                {
                    return new ResponseInterface("La contraseña no coincide con este usuario", response.PartialContent, true);
                }

                AuthData auth = new AuthData(usuario);

                Usuarioporperfil? usuarioporperfil = this.DbContext.Usuarioporperfil.Include(p => p.Perfil).Where(p => p.UsuarioId == usuario.UsuarioId).FirstOrDefault();
                if (usuarioporperfil == null)
                {
                    return new ResponseInterface("El usuario no tiene un perfil válido", response.PartialContent, true);
                }

                auth.PerfilId = usuarioporperfil.PerfilId;
                auth.TiempoSesion = usuarioporperfil.Perfil.TiempoSesion;
                auth.TiempoInactividad = usuarioporperfil.Perfil.TiempoInactividad;

                Sesion? sesion = null;
                DateTime hoy = DateTime.Now;
                if (usuarioporperfil.Perfil.SesionesSimultaneas == 0)
                {
                    sesion = this.DbContext.Sesion.Where(p => p.UsuarioId == usuario.UsuarioId && p.Token != null && p.Inicia <= hoy && hoy <= p.Finaliza)
                        .OrderBy(p => p.SesionId).FirstOrDefault();
                    if (sesion != null)
                    {
                        return new ResponseInterface("Ya existe una sesión activa para este usuario", response.PartialContent, true);
                    }
                }
                else
                {
                    List<Sesion> sesiones = this.DbContext.Sesion.Where(p => p.UsuarioId == usuario.UsuarioId && p.Token != null && p.Inicia <= hoy && hoy <= p.Finaliza)
                        .OrderBy(p => p.SesionId).ToList();
                    if (sesiones.Count >= 2)
                    {
                        return new ResponseInterface("Ya existen 2 sesiónes activas para este usuario", response.PartialContent, true);
                    }
                }

                if (sesion == null)
                {
                    this.CrearSesion(auth, usuario, data);
                }
                else
                {
                    auth.SetSesion(sesion);
                    Bearer bearer = new Bearer
                    {
                        UsuarioId = usuario.UsuarioId,
                        SesionId = sesion.SesionId
                    };
                    auth.Bearer = this.firewall.Encode(bearer);
                }
                return new ResponseInterface("", response.OK, false, auth);
            }
            catch (Exception ex)
            {
                return new ResponseInterface("Error al iniciar sesión", response.PartialContent, true);
            }
        }

        public void CrearSesion(AuthData auth, Usuario usuario, UsuarioLogin data)
        {
            using (IDbContextTransaction transaction = this.DbContext.Database.BeginTransaction())
            {
                try
                {
                    Usuarioporperfil? usuarioporperfil = this.DbContext.Usuarioporperfil.Include(p => p.Perfil).Where(p => p.UsuarioId == usuario.UsuarioId).FirstOrDefault();
                    string token = this.firewall.Encode(auth);
                    dynamic minutos = usuarioporperfil.Perfil.TiempoSesion;
                    DateTime hoy = DateTime.Now;
                    DateTime vence = hoy.AddMinutes(minutos);

                    Sesion sesion = new Sesion();
                    sesion.UsuarioId = usuario.UsuarioId;
                    sesion.Token = token;
                    sesion.Inicia = hoy;
                    sesion.Finaliza = vence;
                    auth.SetSesion(sesion);

                    this.DbContext.Add(sesion);
                    this.DbContext.SaveChanges();

                    Bearer bearer = new Bearer();
                    bearer.UsuarioId = usuario.UsuarioId;
                    bearer.SesionId = sesion.SesionId;
                    auth.Bearer = this.firewall.Encode(bearer);

                    transaction.Commit();
                }
                catch(Exception ex)
                {
                    transaction.Rollback();
                }
            }
        }

        public ResponseInterface CambioContrasena(Response response, bool JWE = false)
        {
            try
            {
                RecuperarContrasena data = request.GetData<RecuperarContrasena>();

                Usuario? usuario = this.DbContext.Usuario.Where(p => p.UsuarioId == data.UsuarioId).FirstOrDefault();
                string claveAnterior = Helper.CalculateMD5Hash(data.ClaveAnterior);
                if (usuario.Contrasena.ToLower() != claveAnterior.ToLower())
                {
                    return new ResponseInterface("La contraseña actual no coincide con esta cuenta", response.PartialContent, true);
                }
                usuario.ReiniciarContrasena = 0;
                string clave = Helper.CalculateMD5Hash(data.Clave);
                usuario.Contrasena = clave;

                this.DbContext.Update(usuario);
                this.DbContext.SaveChanges();

                return new ResponseInterface("Contraseña cambiada correctamente", response.OK, false);
            }
            catch (Exception ex)
            {
                return new ResponseInterface("Error al cambiar la contraseña", response.NotFound, true);
            }
        }

        public ResponseInterface Logout(Response response)
        {
            try
            {
                Bearer data = request.GetData<Bearer>();
                Sesion? sesion = this.DbContext.Sesion.Where(p => p.SesionId == data.SesionId).FirstOrDefault();
                sesion.Token = null;
                this.DbContext.Update(sesion);
                this.DbContext.SaveChanges();
                return new ResponseInterface("", response.OK, false);
            }
            catch(Exception ex)
            {
                return new ResponseInterface("Sesión no encontrada", response.NotFound, true);
            }
        }
    }
}
