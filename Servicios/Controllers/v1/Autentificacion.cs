using Servicios.Security.Partial;
using Servicios.Security;
using Servicios.Models;

namespace Servicios.Controllers.v1
{
    public class Autentificacion
    {
        public static readonly string mensajeNoAutentificado = "No tienes permiso para realizar esta petición.";

        public Autentificacion(EcoTrackContext DbContext)
        {
        }

        public static Bearer GetUsuarioToken(IHeaderDictionary headers)
        {
            var header = headers["Authorization"];

            Firewall firewall = new Firewall();
            Bearer usuarioToken = firewall.Decode<Bearer>(header);

            return usuarioToken;
        }
    }
}
