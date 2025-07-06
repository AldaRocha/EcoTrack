using System.ComponentModel.DataAnnotations;

namespace Servicios.Models.Tables
{
    public class TipoUsuario
    {
        [Key]
        public int TipoUsuarioId { get; set; }
        public string Nombre { get; set; }
        public string Expresion { get; set; }
        public string ComentarioExpresion { get; set; }
        public byte Activo { get; set; }
    }
}
