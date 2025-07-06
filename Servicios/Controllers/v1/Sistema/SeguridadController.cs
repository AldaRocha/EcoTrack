using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore.Storage;
using Servicios.Models;
using Servicios.Models.Tables;
using Servicios.Models.ViewModel;
using Servicios.Security;
using Servicios.Security.Modules;

namespace Servicios.Controllers.v1.Sistema
{
    [Route("api/v1/seguridad")]
    public class SeguridadController : ControllerBase
    {
        #region Variables

        EcoTrackContext DbContext;
        Response response;

        #endregion

        #region Constructor

        public SeguridadController(EcoTrackContext DbContext)
        {
            this.DbContext = DbContext;
            this.response = new Response();
        }

        #endregion

        #region Usuarios

        [HttpPost("usuario")]
        public ActionResult<Response> GuardarUsuario([FromBody] RequestInterface request)
        {
            using (IDbContextTransaction transaction = this.DbContext.Database.BeginTransaction())
            {
                try
                {
                    UsuarioModel usuarioModel = request.GetData<UsuarioModel>();

                    Usuario existe = this.DbContext.Usuario.Where(p => p.Cuenta == usuarioModel.cuenta).FirstOrDefault();
                    if (existe != null)
                    {
                        this.HttpContext.Response.StatusCode = response.PartialContent;
                        return new ObjectResult(new { message = "Este nombre de usuario ya existe" });
                    }

                    Persona persona = new Persona();
                    persona.Nombre = usuarioModel.persona.Nombre;
                    persona.ApellidoPaterno = usuarioModel.persona.ApellidoPaterno;
                    persona.ApellidoMaterno = usuarioModel.persona.ApellidoMaterno;
                    persona.Celular = usuarioModel.persona.Celular;
                    persona.FechaNacimiento = usuarioModel.persona.FechaNacimiento;
                    persona.Email = usuarioModel.persona.Email;
                    persona.FechaRegistro = DateTime.Now;
                    persona.PaisId = usuarioModel.persona.PaisId;

                    this.DbContext.Add(persona);
                    this.DbContext.SaveChanges();

                    Usuario usuario = new Usuario();
                    usuario.Cuenta = usuarioModel.cuenta;
                    string clave = Helper.CalculateMD5Hash(usuarioModel.contrasena);
                    usuario.Contrasena = clave;
                    usuario.ReiniciarContrasena = 1;
                    usuario.Terminos = usuarioModel.terminos;
                    usuario.Activo = usuarioModel.activo;
                    usuario.PersonaId = persona.PersonaId;
                    usuario.TipoUsuarioId = usuarioModel.tipoUsuarioId;

                    this.DbContext.Add(usuario);
                    this.DbContext.SaveChanges();

                    Usuarioporperfil usuarioporperfil = new Usuarioporperfil();
                    usuarioporperfil.UsuarioId = usuario.UsuarioId;
                    usuarioporperfil.PerfilId = usuarioModel.PerfilId;

                    this.DbContext.Add(usuarioporperfil);
                    this.DbContext.SaveChanges();

                    transaction.Commit();

                    return response.Ok("Se ha creado el usuario");
                }
                catch (Exception ex)
                {
                    transaction.Rollback();
                    this.HttpContext.Response.StatusCode = response.BadRequest;
                    return new ObjectResult(new { message = ex.Message });
                }
            }
        }

        #endregion
    }
}
