using Servicios.Models.Tables;

namespace Servicios.Security.Partial
{
    public class AuthData
    {
        public int UsuarioId { get; set; }
        public string Usuario { get; set; }
        public int PerfilId { get; set; }
        public int TiempoSesion { get; set; }
        public int TiempoInactividad { get; set; }
        public Sesion sesion { get; set; }
        public string Bearer { get; set; }

        public AuthData(Usuario usuario)
        {
            this.UsuarioId = usuario.UsuarioId;
            this.Usuario = usuario.Cuenta;
        }

        public void SetSesion(Sesion sesion)
        {
            this.sesion = sesion;
        }
    }
}
