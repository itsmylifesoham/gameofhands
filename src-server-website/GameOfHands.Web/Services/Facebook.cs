using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Configuration;
using GameOfHands.Web.Models.Facebook;
using GameOfHands.Web.Models.Login;
using Newtonsoft.Json;
using GameOfHands.Web.Models;
using System.Net.Http;
using GameOfHands.Web.Models.User;

namespace GameOfHands.Web.Services
{
    public class Facebook
    {
        public static async Task<DebugTokenData> GetFacebookAccessTokenDebugInfo(string inputToken)
        {
            var response = await HttpClientPool.GetHttpClient().GetAsync(FacebookApi.GetDebugTokenUrl(inputToken));

            if (response.IsSuccessStatusCode)
            {
                var responseJson = await response.Content.ReadAsStringAsync();
                var debugTokenData = JsonConvert.DeserializeObject<DebugTokenDataRoot>(responseJson).data;
                return debugTokenData;
            }
            else
            {
                throw new Exception("Could not retrieve facebook access token debug info from facebook");
            }
        }

        public static async Task<string> ExchangeTokenForLongLivedToken(string tokenToExchange)
        {            
            var response = await HttpClientPool.GetHttpClient().GetAsync(FacebookApi.GetExchangeTokenUrl(tokenToExchange));

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

        public static async Task<UserProfileInfo> GetUserProfileInfo(string userId, string userAccessToken)
        {
            var response = await HttpClientPool.GetHttpClient().GetAsync(FacebookApi.GetBasicUserInfoUrl(userId, userAccessToken));

            if (response.IsSuccessStatusCode)
            {
                var responseJson = await response.Content.ReadAsStringAsync();
                var userProfileInfo = JsonConvert.DeserializeObject<UserProfileInfo>(responseJson);
                return userProfileInfo;
            }
            else
            {
                return null;
            }
        }
    }
}