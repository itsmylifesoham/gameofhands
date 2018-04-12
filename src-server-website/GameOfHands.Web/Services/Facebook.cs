using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Configuration;
using GameOfHands.Web.Models.Facebook;
using GameOfHands.Web.Models.Login;
using Newtonsoft.Json;

namespace GameOfHands.Web.Services
{
    public class Facebook
    {
        private async Task<DebugTokenData> GetFacebookAccessTokenDebugInfo(string inputToken)
        {
            var response = await HttpClientPool.GetHttpClient().GetAsync();

            if (response.IsSuccessStatusCode)
            {
                var responseJson = await response.Content.ReadAsStringAsync();
                var debugTokenData = JsonConvert.DeserializeObject<DebugTokenDataRoot>(responseJson).data;
                return debugTokenData;
            }
            else
            {
                throw new Exception("Could not get facebook access token debug info");
            }
        }

        private async Task<string> ExchangeTokenForLongLivedToken(string access_token)
        {
            var response = await HttpClientPool.GetHttpClient().GetAsync($"{FacebookConfiguration.FacebookApiEndpoint}/oauth/access_token?access_token={WebConfigurationManager.AppSettings["facebookAppAccessToken"]}&grant_type=fb_exchange_token&client_id={WebConfigurationManager.AppSettings["facebookAppId"]}&client_secret={WebConfigurationManager.AppSettings["facebookAppSecret"]}&fb_exchange_token={access_token}");

            if (response.IsSuccessStatusCode)
            {
                var responseJson = await response.Content.ReadAsStringAsync();
                var exchangeTokenData = JsonConvert.DeserializeObject<ExchangeTokenData>(responseJson);
                return exchangeTokenData.access_token;
            }
            else
            {
                return null;
            }
        }
    }
}