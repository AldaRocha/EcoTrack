using System.ComponentModel.DataAnnotations;

namespace Servicios.Models.Tables
{
    public class Sesion
    {
        [Key]
        public int SesionId { get; set; }
        public string? Token { get; set; }
        public DateTime Inicia { get; set; }
        public DateTime? Finaliza { get; set; }
        public int UsuarioId { get; set; }
        public virtual Usuario Usuario { get; set; }
    }
}
