using System.ComponentModel.DataAnnotations;

namespace Servicios.Models.Tables
{
    public class Perfil
    {
        [Key]
        public int PerfilId { get; set; }
        public string Nombre { get; set; }
        public int TiempoSesion { get; set; }
        public int TiempoInactividad { get; set; }
        public byte SesionesSimultaneas { get; set; }
        public byte Activo { get; set; }
    }
}
