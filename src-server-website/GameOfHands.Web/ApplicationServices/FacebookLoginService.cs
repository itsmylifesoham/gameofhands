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
        /// <summary>
        /// Provides facebook login functionality
        /// </summary>
        /// <param name="accessToken"></param>
        /// <param name="userId"></param>
        /// <param name="deviceId">Identifies the operating system and unique id for the device. Is helpful to see how many devices the user is logging in from or how many installs we have</param>
        /// <param name="ipAddress"></param>
        /// <returns></returns>
        public static async Task<LoginResult> Login(string accessToken, string userId, string deviceId, string ipAddress)
        {
            try
            {
                var userLoginId = LoginIdGenerator.GetLoginId(userId, LoginType.Facebook);
                EnsureAccessTokenNotEmpty(accessToken);
                var tokenDebugInfo = await Facebook.GetFacebookAccessTokenDebugInfo(accessToken);

                if (!tokenDebugInfo.IsValid() || tokenDebugInfo.user_id != userId)
                    return LoginResult.CreateFailed("Invalid Access Token. " + tokenDebugInfo.GetErrorMessage());

                string tokenToReturn = accessToken;
                if (tokenDebugInfo.IsTokenExpiringInLessThanOneDay())
                {
                    tokenToReturn = await Facebook.ExchangeTokenForLongLivedToken(accessToken);
                    await Database.UpdateUserAccessToken(userLoginId, tokenToReturn);
                }


                var sfsToken = await Database.CreateSfsSession(userId, deviceId, ipAddress, LoginType.Facebook, tokenToReturn);

                return LoginResult.CreateSuccess(new
                {
                    newToken = tokenToReturn,
                    userLoginId = LoginIdGenerator.GetLoginId(userId, Models.Login.LoginType.Facebook),
                    deviceId = deviceId,
                    userId = userId,
                    sfsToken = sfsToken
                });
            }
            catch (Exception)
            {
                return LoginResult.CreateFailed("Unable to login.");
            }
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
