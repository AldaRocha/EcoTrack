using System.ComponentModel.DataAnnotations;

namespace Servicios.Models.Tables
{
    public class Usuario
    {
        [Key]
        public int UsuarioId { get; set; }
        public string Cuenta { get; set; }
        public string Contrasena { get; set; }
        public byte ReiniciarContrasena { get; set; }
        public byte Terminos { get; set; }
        public byte Activo { get; set; }
        public int PersonaId { get; set; }
        public virtual Persona Persona { get; set; }
        public int TipoUsuarioId { get; set; }
        public virtual TipoUsuario TipoUsuario { get; set; }
    }
}
