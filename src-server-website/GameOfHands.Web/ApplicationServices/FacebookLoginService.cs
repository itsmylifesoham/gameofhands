using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web.Configuration;
using GameOfHands.Web.Models.Facebook;
using GameOfHands.Web.Models.Login;
using GameOfHands.Web.Services;

namespace GameOfHands.Web.ApplicationServices
{
    public class FacebookLoginService
    {
        public static async Task<LoginResult> Login(string accessToken, string userId, string deviceId, string ipAddress)
        {
            try
            {
                EnsureAccessTokenNotEmpty(accessToken);
                var tokenDebugInfo = await Facebook.GetFacebookAccessTokenDebugInfo(accessToken);

                if (!tokenDebugInfo.IsValid() || tokenDebugInfo.user_id != userId)
                    return LoginResult.CreateFailed("Invalid Access Token. " + tokenDebugInfo.GetErrorMessage());

                var tokenToReturn = tokenDebugInfo.IsTokenExpiringInLessThanOneDay() ? Facebook.ExchangeTokenForLongLivedToken(accessToken).Result : accessToken;
                await Database.CreateSfsSession(LoginIdGenerator.GetFacebookLoginId(userId), deviceId, ipAddress);

                return LoginResult.CreateSuccess(new
                {
                    newToken = tokenToReturn,
                    userLoginId = LoginIdGenerator.GetFacebookLoginId(userId),
                    userId = userId
                });
            }
            catch (Exception e)
            {
                return LoginResult.CreateFailed("Unable to login.");                
            }            
        }

        private async Task<string> GetExchangedToken(string accessToken)
        {
            var newToken = accessToken;
            newToken = await Facebook.ExchangeTokenForLongLivedToken(accessToken);
            if (newToken == null)
            {
                newToken = accessToken;
            }

            return newToken;
        }

        private static void EnsureAccessTokenNotEmpty(string accessToken)
        {
            if (string.IsNullOrEmpty(accessToken))
            {
                throw new Exception("Got empty access token.");
            }
        }






    }
}
