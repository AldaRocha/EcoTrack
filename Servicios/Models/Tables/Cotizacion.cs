using System.ComponentModel.DataAnnotations;

namespace Servicios.Models.Tables
{
    public class Cotizacion
    {
       
        [Key]
        public int CotizacionId { get; set; }
        public string Nombre { get; set; }
        public string Telefono { get; set; }
        public string Email { get; set; }
        public string ProductoIoT { get; set; }
        public string ApiIA { get; set; }
        public string UsoPlataforma { get; set; }
        public DateTime FechaRegistro { get; set; }
}
}