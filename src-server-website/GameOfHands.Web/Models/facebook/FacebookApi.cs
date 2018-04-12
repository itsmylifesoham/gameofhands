using System;
using System.Collections.Generic;
using System.Dynamic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web.Configuration;

namespace GameOfHands.Web.Models.Facebook
{
    public static class FacebookApi
    {
        private static string FacebookApiEndpoint = "https://graph.facebook.com/v2.12";

        public static string GetDebugTokenUrl(string inputToken)
        {
            return
                $"{FacebookApiEndpoint}/debug_token?access_token={WebConfigurationManager.AppSettings["facebookAppAccessToken"]}&input_token={inputToken}";
        }

        public static string GetExchangeTokenUrl(string tokenToExchange)
        {
            return
                $"{FacebookApiEndpoint}/oauth/access_token?access_token={WebConfigurationManager.AppSettings["facebookAppAccessToken"]}&grant_type=fb_exchange_token&client_id={WebConfigurationManager.AppSettings["facebookAppId"]}&client_secret={WebConfigurationManager.AppSettings["facebookAppSecret"]}&fb_exchange_token={tokenToExchange}";
        }
    }
}
