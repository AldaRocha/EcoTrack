using Servicios.Models.Tables;

namespace Servicios.Models.ViewModel
{
    public class UsuarioModel
    {
        public int usuarioId { get; set; }
        public string cuenta { get; set; }
        public string contrasena { get; set; }
        public byte reiniciarContrasena { get; set; }
        public byte terminos { get; set; }
        public byte activo { get; set; }
        public int personaId { get; set; }
        public int PerfilId { get; set; }
        public Persona persona { get; set; }
        public int tipoUsuarioId { get; set; }
        public TipoUsuario tipoUsuario { get; set; }
    }
}
