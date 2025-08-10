using MailKit.Net.Smtp;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using MimeKit;
using Servicios.Models;
using Servicios.Models.Tables;
using Servicios.Models.ViewModel;

namespace Servicios.Controllers.v1.Sistema
{
    [Route("api/v1/quotation")]
    public class QuotationController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        private readonly EcoTrackContext _dbContext; 

        public QuotationController(IConfiguration configuration, EcoTrackContext dbContext)
        {
            _configuration = configuration;
            _dbContext = dbContext; 
        }

        [HttpPost]
        public async Task<IActionResult> SendQuotation([FromBody] QuotationModel model)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(new { success = false, message = "Datos de solicitud inválidos" });
            }

            try
            {
                // Guardar en base de datos
                var cotizacion = new Cotizacion
                {
                    Nombre = model.Nombre,
                    Telefono = model.Telefono,
                    Email = model.Email,
                    ProductoIoT = model.ProductoIoT,
                    ApiIA = model.ApiIA,
                    UsoPlataforma = model.UsoPlataforma,
                    FechaRegistro = DateTime.UtcNow
                };

                _dbContext.Cotizaciones.Add(cotizacion);
                await _dbContext.SaveChangesAsync();

                // Enviar correos
                await SendQuotationEmails(model);

                return Ok(new
                {
                    success = true,
                    message = "Cotización enviada y guardada correctamente.",
                    cotizacionId = cotizacion.CotizacionId
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new
                {
                    success = false,
                    message = "Error interno del servidor",
                    detail = ex.Message
                });
            }
        }

        private async Task SendQuotationEmails(QuotationModel model)
        {
            var smtpHost = _configuration["EmailSettings:SmtpServer"] ?? "smtp.gmail.com";
            var smtpPort = _configuration.GetValue<int>("EmailSettings:Port", 587);
            var fromEmail = _configuration["EmailSettings:Username"] ?? "ecodev538@gmail.com";
            var fromName = _configuration["EmailSettings:FromName"] ?? "EcoDev";
            var password = _configuration["EmailSettings:Password"] ?? "fhmf cyxc fbvq tvtl";

            // Mensaje para EcoDev
            var companyMessage = new MimeMessage();
            companyMessage.From.Add(new MailboxAddress(fromName, fromEmail));
            companyMessage.To.Add(new MailboxAddress("EcoDev Admin", "ecodev538@gmail.com"));
            companyMessage.Subject = "Nueva solicitud de cotización";

            var companyBodyBuilder = new BodyBuilder();
            companyBodyBuilder.HtmlBody = $@"
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body {{ font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; }}
                        .container {{ background-color: #fff; border-radius: 8px; padding: 20px; margin: 20px auto; max-width: 600px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }}
                        h2 {{ color: #2e8b57; }}
                        p {{ color: #555; text-align: left; }}
                        ul {{ text-align: left; list-style-type: none; padding: 0; }}
                        li {{ background-color: #e8f5e9; margin: 5px 0; padding: 10px; border-radius: 5px; }}
                    </style>
                </head>
                <body>
                    <div class='container'>
                        <h2>Nueva Solicitud de Cotización para EcoDev</h2>
                        <p><strong>Nombre:</strong> {model.Nombre}</p>
                        <p><strong>Teléfono:</strong> {model.Telefono}</p>
                        <p><strong>Email:</strong> {model.Email}</p>
                        <p><strong>Opciones seleccionadas:</strong></p>
                        <ul>
                            <li><strong>Producto IoT:</strong> {model.ProductoIoT}</li>
                            <li><strong>API de la IA:</strong> {model.ApiIA}</li>
                            <li><strong>Uso de plataforma:</strong> {model.UsoPlataforma}</li>
                        </ul>
                    </div>
                </body>
                </html>";

            companyMessage.Body = companyBodyBuilder.ToMessageBody();

            // Mensaje para el cliente
            var userMessage = new MimeMessage();
            userMessage.From.Add(new MailboxAddress(fromName, fromEmail));
            userMessage.To.Add(new MailboxAddress(model.Nombre, model.Email));
            userMessage.Subject = "Confirmación de tu solicitud de cotización";

            var userBodyBuilder = new BodyBuilder();
            userBodyBuilder.HtmlBody = $@"
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body {{ font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; }}
                        .container {{ background-color: #fff; border-radius: 8px; padding: 20px; margin: 20px auto; max-width: 600px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }}
                        h2 {{ color: #2e8b57; }}
                        p {{ color: #555; text-align: left; }}
                        ul {{ text-align: left; list-style-type: none; padding: 0; }}
                        li {{ background-color: #e8f5e9; margin: 5px 0; padding: 10px; border-radius: 5px; }}
                    </style>
                </head>
                <body>
                    <div class='container'>
                        <h2>Hola, {model.Nombre}</h2>
                        <p>Hemos recibido tu solicitud de cotización. En breve, un miembro de nuestro equipo se pondrá en contacto contigo.</p>
                        <p>Estos son los detalles que nos proporcionaste:</p>
                        <ul>
                            <li><strong>Producto IoT:</strong> {model.ProductoIoT}</li>
                            <li><strong>API de la IA:</strong> {model.ApiIA}</li>
                            <li><strong>Uso de plataforma:</strong> {model.UsoPlataforma}</li>
                        </ul>
                        <p>¡Gracias por tu interés en EcoDev!</p>
                    </div>
                </body>
                </html>";

            userMessage.Body = userBodyBuilder.ToMessageBody();

            // Enviar correos
            using (var client = new SmtpClient())
            {
                await client.ConnectAsync(smtpHost, smtpPort, MailKit.Security.SecureSocketOptions.StartTls);
                await client.AuthenticateAsync(fromEmail, password);
                await client.SendAsync(companyMessage);
                await client.SendAsync(userMessage);
                await client.DisconnectAsync(true);
            }
        }
    }
}