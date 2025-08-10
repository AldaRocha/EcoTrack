using MailKit.Net.Smtp;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using MimeKit;
using Servicios.Models;
using Servicios.Models.Tables;
using Servicios.Models.ViewModel;
using System.Text.RegularExpressions;

namespace Servicios.Controllers.v1.Sistema
{
    [Route("api/v1/subscription")]
    public class SubscriptionController : ControllerBase
    {
        private readonly EcoTrackContext _dbContext;
        private readonly IConfiguration _configuration;

        public SubscriptionController(EcoTrackContext dbContext, IConfiguration configuration)
        {
            _dbContext = dbContext;
            _configuration = configuration;
        }

        [HttpPost]
        public async Task<IActionResult> Subscribe([FromBody] SubscriptionModel model)
        {
            // Validación básica del modelo
            if (model == null)
            {
                return BadRequest(new { success = false, message = "Datos de solicitud inválidos" });
            }

            // Validación manual del email
            try
            {
                var addr = new System.Net.Mail.MailAddress(model.Email);
                if (addr.Address != model.Email)
                {
                    return BadRequest(new { success = false, message = "Formato de email inválido" });
                }
            }
            catch
            {
                return BadRequest(new { success = false, message = "Formato de email inválido" });
            }

            try
            {
                // Verificar si el email ya existe
                if (await _dbContext.Subscription.AnyAsync(s => s.Email == model.Email))
                {
                    return Conflict(new
                    {
                        success = false,
                        message = "Este email ya está registrado",
                        email = model.Email
                    });
                }

                // Guardar en base de datos
                var subscription = new Subscription
                {
                    Email = model.Email,
                    AcceptsNotifications = model.AcceptsNotifications,
                    SubscriptionDate = DateTime.UtcNow, 
                    ActivationToken = Guid.NewGuid().ToString(),
                    IsActive = model.AcceptsNotifications
                };

                _dbContext.Subscription.Add(subscription);
                await _dbContext.SaveChangesAsync();

                // Enviar correos (manejado en un método separado)
                await SendConfirmationEmails(model.Email, subscription.ActivationToken, model.AcceptsNotifications);

                return Ok(new
                {
                    success = true,
                    message = "Suscripción exitosa",
                    email = model.Email
                });
            }
            catch (DbUpdateException dbEx)
            {
                return StatusCode(500, new
                {
                    success = false,
                    message = "Error al guardar en base de datos",
                    detail = dbEx.InnerException?.Message
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

        private async Task SendConfirmationEmails(string email, string token, bool acceptsNotifications)
        {
            try
            {
                // Configuración del servidor SMTP
                var smtpHost = _configuration["EmailSettings:SmtpServer"] ?? "smtp.gmail.com";
                var smtpPort = _configuration.GetValue<int>("EmailSettings:Port", 587);
                var fromEmail = _configuration["EmailSettings:Username"] ?? "ecodev538@gmail.com";
                var fromName = _configuration["EmailSettings:FromName"] ?? "EcoDev";
                var password = _configuration["EmailSettings:Password"] ?? "fhmf cyxc fbvq tvtl";

                // Mensaje para la empresa
                var companyMessage = new MimeMessage();
                companyMessage.From.Add(new MailboxAddress(fromName, fromEmail));
                companyMessage.To.Add(new MailboxAddress("EcoDev Admin", "ecodev538@gmail.com"));
                companyMessage.Subject = "Nueva suscripción - " + DateTime.UtcNow.ToString("yyyy-MM-dd");
                companyMessage.Body = new TextPart("plain")
                {
                    Text = $"Nuevo suscriptor:\nEmail: {email}\nAcepta notificaciones: {acceptsNotifications}\nFecha: {DateTime.UtcNow}"
                };

                // Mensaje para el usuario
                var userMessage = new MimeMessage();
                userMessage.From.Add(new MailboxAddress(fromName, fromEmail));
                userMessage.To.Add(new MailboxAddress("", email));
                userMessage.Subject = "Confirmación de suscripción - EcoDev";

                var userBodyBuilder = new BodyBuilder();

                if (acceptsNotifications)
                {
                    userBodyBuilder.HtmlBody = @"
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body { font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; padding: 20px; }
                        .container { background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); max-width: 600px; margin: auto; }
                        .header { color: #333333; }
                        .content { color: #666666; margin-top: 20px; }
                    </style>
                </head>
                <body>
                    <div class='container'>
                        <h2 class='header'>¡Suscripción confirmada!</h2>
                        <p class='content'>Gracias por suscribirte a nuestras notificaciones. Recibirás nuestras novedades en tu bandeja de entrada.</p>
                    </div>
                </body>
                </html>";
                }
                else
                {
                    var activationUrl = $"{_configuration["AppSettings:BaseUrl"]}/api/v1/subscription/activate?email={email}&token={token}";

                    userBodyBuilder.HtmlBody = $@"
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body {{ font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; padding: 20px; }}
                        .container {{ background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); max-width: 600px; margin: auto; }}
                        .header {{ color: #333333; }}
                        .content {{ color: #666666; margin-top: 20px; }}
                        .button-link {{ background-color: #4CAF50; color: white; padding: 15px 25px; text-decoration: none; border-radius: 5px; font-weight: bold; }}
                    </style>
                </head>
                <body>
                    <div class='container'>
                        <h2 class='header'>¡Gracias por registrarte en EcoDev!</h2>
                </body>
                </html>";
                }

                userMessage.Body = userBodyBuilder.ToMessageBody();

                // Enviar ambos correos
                using (var client = new SmtpClient())
                {
                    await client.ConnectAsync(smtpHost, smtpPort, MailKit.Security.SecureSocketOptions.StartTls);
                    await client.AuthenticateAsync(fromEmail, password);
                    await client.SendAsync(companyMessage);
                    await client.SendAsync(userMessage);
                    await client.DisconnectAsync(true);
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error enviando correos: {ex.Message}");
            }
        }

        [HttpPost("activate")]
        public async Task<IActionResult> ActivateNotifications([FromQuery] string email, [FromQuery] string token)
        {
            var subscription = await _dbContext.Subscription
                .FirstOrDefaultAsync(s => s.Email == email && s.ActivationToken == token);

            if (subscription == null)
                return NotFound(new { success = false, message = "Suscripción no encontrada" });

            subscription.AcceptsNotifications = true;
            subscription.IsActive = true;
            await _dbContext.SaveChangesAsync();

            return Ok(new { success = true, message = "Notificaciones activadas correctamente" });
        }

        private async Task SendEmailToCompany(string userEmail)
        {
            var message = new MimeMessage();
            message.From.Add(new MailboxAddress("Sistema EcoDev", "ecodev538@gmail.com"));
            message.To.Add(new MailboxAddress("EcoDev", "ecodev538@gmail.com"));
            message.Subject = "Nueva suscripción";
            message.Body = new TextPart("plain")
            {
                Text = $"Nuevo suscriptor: {userEmail}\nFecha: {DateTime.Now}"
            };

            await SendEmail(message);
        }

        private async Task SendConfirmationEmail(string email, string token, bool acceptsNotifications)
        {
            var message = new MimeMessage();
            message.From.Add(new MailboxAddress("EcoDev", "ecodev538@gmail.com"));
            message.To.Add(new MailboxAddress("", email));
            message.Subject = "Confirmación de suscripción";

            var body = acceptsNotifications
                ? "Gracias por suscribirte a nuestras notificaciones."
                : $"Gracias por registrarte. Puedes activar notificaciones más adelante visitando:  http://localhost:5063/api/v1/subscription/activate?email={email}&token={token}";

            message.Body = new TextPart("plain") { Text = body };

            await SendEmail(message);
        }

        private async Task SendEmail(MimeMessage message)
        {
            using (var client = new SmtpClient())
            {
                await client.ConnectAsync("smtp.gmail.com", 587, false);
                await client.AuthenticateAsync("ecodev538@gmail.com", "fhmf cyxc fbvq tvtl");
                await client.SendAsync(message);
                await client.DisconnectAsync(true);
            }
        }
    }
}