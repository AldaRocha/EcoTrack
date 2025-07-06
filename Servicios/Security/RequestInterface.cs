namespace Servicios.Security
{
    public class RequestInterface
    {
        public string Token { get; set; }
        private Firewall firewall;

        public RequestInterface()
        {
            this.firewall = new Firewall();
        }

        public T GetData<T>(bool JWE = false)
        {
            if (JWE)
            {
                this.firewall.JWE = true;
            }
            return this.firewall.Decode<T>(this.Token);
        }
    }
}
