using System.ComponentModel.DataAnnotations;

namespace Servicios.Models.Tables
{
    public class Subscription
    {
        [Key]
        public int SubscriptionId { get; set; }
        public string Email { get; set; }
        public bool AcceptsNotifications { get; set; }
        public DateTime SubscriptionDate { get; set; }
        public string ActivationToken { get; set; }
        public bool IsActive { get; set; }
    }
}
