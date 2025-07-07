using System.ComponentModel.DataAnnotations;

namespace Servicios.Models.Tables
{
    public class Persona
    {
        [Key]
        public int PersonaId { get; set; }
        public string Nombre { get; set; }
        public string ApellidoPaterno { get; set; }
        public string ApellidoMaterno { get; set; }
        public string Celular { get; set; }
        public DateTime FechaNacimiento { get; set; }
        public string Email { get; set; }
        public DateTime FechaRegistro { get; set; }
        public int PaisId { get; set; }
        public virtual Pais Pais { get; set; }
    }
}
