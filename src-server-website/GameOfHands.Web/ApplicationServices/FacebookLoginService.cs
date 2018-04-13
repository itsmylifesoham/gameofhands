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
       
        public static async Task<LoginResult> Login(string accessToken, string ipAddress, LoginSource loginSource)
        {
            try
            {
                var userLoginId = loginSource.GenerateAppScopedLoginId();
                EnsureAccessTokenNotEmpty(accessToken);
                var tokenDebugInfo = await Facebook.GetFacebookAccessTokenDebugInfo(accessToken);

                if (!tokenDebugInfo.IsValid() || tokenDebugInfo.user_id != loginSource.UserId)
                    return LoginResult.CreateFailed("Invalid Access Token. " + tokenDebugInfo.GetErrorMessage());

                 


                var sfsToken = await Database.CreateSfsSession(loginSource, ipAddress, accessToken);

                string tokenToReturn = accessToken;
                if (tokenDebugInfo.IsTokenExpiringInLessThanOneDay())
                {
                    tokenToReturn = await Facebook.ExchangeTokenForLongLivedToken(accessToken);
                    await Database.UpdateUserAccessToken(userLoginId, tokenToReturn);
                }

                return LoginResult.CreateSuccess(new
                {
                    newAccessToken = tokenToReturn == accessToken ? null : tokenToReturn,
                    userLoginId = loginSource.GenerateAppScopedLoginId(),         
                    sfsToken = sfsToken
                });
            }
            catch (Exception e)
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
