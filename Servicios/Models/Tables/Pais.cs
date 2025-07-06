using System.ComponentModel.DataAnnotations;

namespace Servicios.Models.Tables
{
    public class Pais
    {
        [Key]
        public int PaisId { get; set; }
        public string Nombre { get; set; }
        public byte Activo { get; set; }
    }
}
