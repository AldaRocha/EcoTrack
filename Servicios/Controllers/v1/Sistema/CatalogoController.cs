using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.IdentityModel.Tokens;
using Servicios.Models;
using Servicios.Models.Tables;
using Servicios.Models.ViewModel;
using Servicios.Security;

namespace Servicios.Controllers.v1.Sistema
{
    [Route("api/v1/catalogo")]
    public class CatalogoController : ControllerBase
    {
        #region Variables

        EcoTrackContext DbContext;
        Response response;

        #endregion

        #region Constructor

        public CatalogoController(EcoTrackContext DbContext)
        {
            this.DbContext = DbContext;
            this.response = new Response();
        }

        #endregion

        #region Pais

        [HttpPost("pais/buscar")]
        public async Task<ActionResult<Response>> BuscarPaises([FromBody] RequestInterface request)
        {
            try
            {
                PaisModel paisData = request.GetData<PaisModel>();

                IQueryable<Pais> paisesQuery = this.DbContext.Pais.AsQueryable();

                if (paisData != null && paisData.PaisId > 0)
                {
                    paisesQuery = paisesQuery.Where(p => p.PaisId == paisData.PaisId);
                }

                if (paisData != null && !paisData.Nombre.IsNullOrEmpty())
                {
                    paisesQuery = paisesQuery.Where(p => p.Nombre.Equals(paisData.Nombre));
                }

                if (paisData !=null && paisData.Activo >= 0)
                {
                    paisesQuery = paisesQuery.Where(p => p.Activo ==  paisData.Activo);
                }

                List<Pais> pais = paisesQuery.ToList();
                
                Dictionary<string, dynamic> res = new Dictionary<string, dynamic>();
                res.Add("pais", pais);

                return response.Ok("", res);
            }
            catch (Exception ex)
            {
                this.HttpContext.Response.StatusCode = response.BadRequest;
                return new ObjectResult(new { message = ex.Message });
            }
        }

        #endregion
    }
}
