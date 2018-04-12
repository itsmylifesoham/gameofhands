using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace GameOfHands.Web.Models.Login
{
    public class HttpClientPool
    {
        private static List<HttpClient> _httpClients = new List<HttpClient>();

        private static int _maxHttpClientCount = 10;

       
        private static int RandomIndex
        {
            get
            {
                return (int)(new DateTime().Ticks % _maxHttpClientCount);
            }
        }


        static HttpClientPool()
        {
            InitHttpClients();
        }

        private static void InitHttpClients()
        {
            for (int i = 0; i < _maxHttpClientCount; i++)
            {
                _httpClients.Add(new HttpClient());
            }
        }

        public static HttpClient GetHttpClient()
        {
            return _httpClients.ElementAt(RandomIndex);
        }
    }
}
