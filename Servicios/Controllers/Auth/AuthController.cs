using Microsoft.AspNetCore.Mvc;
using Servicios.Models;
using Servicios.Security;
using Servicios.Security.Auth;
using Servicios.Security.Partial;

namespace Servicios.Controllers.Auth
{
    [Route("api/auth")]
    public class AuthController : ControllerBase
    {
        #region Variables

        EcoTrackContext DbContext;
        Response response;

        #endregion

        #region Constructor

        public AuthController(EcoTrackContext DbContext)
        {
            this.DbContext = DbContext;
            this.response = new Response();
        }

        #endregion

        #region Metodos

        [HttpPost("login")]
        public ActionResult<Response> Login([FromBody] RequestInterface request)
        {
            try
            {
                bool JWE = this.response.IsJWE();
                ResponseInterface responseInterface = new Authentication(this.DbContext, request).Connect(this.response, JWE);
                if (responseInterface.error)
                {
                    this.HttpContext.Response.StatusCode = responseInterface.status;
                    return this.response.Error(responseInterface.message);
                }
                return this.response.Ok("", responseInterface.authData);
            }
            catch (Exception ex)
            {
                this.HttpContext.Response.StatusCode = response.BadRequest;
                return response.Error(ex.Message);
            }
        }

        [HttpPost("cambio")]
        public ActionResult<Response> CambioContrasena([FromBody] RequestInterface request)
        {
            try
            {
                bool JWE = this.response.IsJWE();
                ResponseInterface responseInterface = new Authentication(this.DbContext, request).CambioContrasena(this.response, JWE);
                if (responseInterface.error)
                {
                    this.HttpContext.Response.StatusCode = responseInterface.status;
                    return this.response.Error(responseInterface.message);
                }
                return this.response.Ok(null, null);
            }
            catch (Exception ex)
            {
                this.HttpContext.Response.StatusCode = response.BadRequest;
                return response.Error(ex.Message);
            }
        }

        [HttpPost("logout")]
        public ActionResult<Response> Logout([FromBody] RequestInterface request)
        {
            try
            {
                ResponseInterface responseInterface = new Authentication(this.DbContext, request).Logout(this.response);
                if (responseInterface.error)
                {
                    this.HttpContext.Response.StatusCode = responseInterface.status;
                    return this.response.Error(responseInterface.message);
                }
                return this.response.Ok("", responseInterface.authData);
            }
            catch (Exception ex)
            {
                this.HttpContext.Response.StatusCode = response.BadRequest;
                return response.Error(ex.Message);
            }
        }

        #endregion
    }
}
