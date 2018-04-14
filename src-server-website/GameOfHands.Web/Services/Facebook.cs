using System;
using System.Threading.Tasks;
using GameOfHands.Web.Models.Facebook;
using GameOfHands.Web.Models.Login;
using Newtonsoft.Json;

namespace GameOfHands.Web.Services
{
    public class Facebook
    {
        public static async Task<DebugTokenData> GetFacebookAccessTokenDebugInfo(string inputToken)
        {
            using (var response =
                await HttpClientPool.GetHttpClient().GetAsync(FacebookApi.GetDebugTokenUrl(inputToken)))
            {
                if (response.IsSuccessStatusCode)
                {
                    var responseJson = await response.Content.ReadAsStringAsync();
                    var debugTokenData = JsonConvert.DeserializeObject<DebugTokenDataRoot>(responseJson).data;
                    return debugTokenData;
                }

                throw new Exception("Could not verify login with facebook. Please try again in some time.");
            }
        }

        public static async Task<string> ExchangeTokenForLongLivedToken(string tokenToExchange)
        {
            using (var response = await HttpClientPool.GetHttpClient()
                .GetAsync(FacebookApi.GetExchangeTokenUrl(tokenToExchange)))
            {
                if (response.IsSuccessStatusCode)
                {
                    var responseJson = await response.Content.ReadAsStringAsync();
                    var exchangeTokenData = JsonConvert.DeserializeObject<ExchangeTokenData>(responseJson);
                    return exchangeTokenData.access_token;
                }
                
                throw new Exception("Could not exchange token for a long lived one.");
            }
        }

        public static async Task<UserProfileInfo> GetUserProfileInfo(string userId, string userAccessToken)
        {
            using (var response = await HttpClientPool.GetHttpClient()
                .GetAsync(FacebookApi.GetBasicUserInfoUrl(userId, userAccessToken)))
            {
                if (response.IsSuccessStatusCode)
                {
                    var responseJson = await response.Content.ReadAsStringAsync();
                    var userProfileInfo = JsonConvert.DeserializeObject<UserProfileInfo>(responseJson);
                    return userProfileInfo;
                }

                throw new Exception("Error fetching user profile info.");
            }
        }
    }
}