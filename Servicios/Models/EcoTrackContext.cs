using Microsoft.EntityFrameworkCore;
using Servicios.Models.Tables;

namespace Servicios.Models
{
    public partial class EcoTrackContext : DbContext
    {
        public EcoTrackContext(DbContextOptions<EcoTrackContext> options) : base(options)
        {

        }

        public DbSet<Usuario> Usuario { get; set; }
        public DbSet<Sesion> Sesion { get; set; }
        public DbSet<Persona> Persona { get; set; }
        public DbSet<Pais> Pais { get; set; }
        public DbSet<TipoUsuario> TipoUsuario { get; set; }
        public DbSet<Usuarioporperfil> Usuarioporperfil { get; set; }
        public DbSet<Perfil> Perfil { get; set; }
        public DbSet<Subscription> Subscription { get; set; }
        public DbSet<Cotizacion> Cotizaciones { get; set; }
    }
}
