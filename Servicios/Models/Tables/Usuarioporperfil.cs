using System.ComponentModel.DataAnnotations;

namespace Servicios.Models.Tables
{
    public class Usuarioporperfil
    {
        [Key]
        public int UsuarioporperfilId { get; set; }
        public int UsuarioId { get; set; }
        public virtual Usuario Usuario { get; set; }
        public int PerfilId { get; set; }
        public virtual Perfil Perfil { get; set; }
    }
}
