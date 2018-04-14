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
       
        public static async Task<LoginResult> Login(string accessToken, string ipAddress, LoginContext loginContext)
        {
            try
            {
                var userLoginId = loginContext.GenerateAppScopedLoginId();
                EnsureAccessTokenNotEmpty(accessToken);
                var tokenDebugInfo = await Facebook.GetFacebookAccessTokenDebugInfo(accessToken);

                if (!tokenDebugInfo.IsValid() || tokenDebugInfo.user_id != loginContext.UserId)
                    throw new Exception("Invalid Access Token. " + tokenDebugInfo.GetErrorMessage());

                var sfsToken = await Database.CreateSfsSession(loginContext, ipAddress, accessToken);

                string tokenToReturn = accessToken;
                if (tokenDebugInfo.IsTokenExpiringInLessThanOneDay())
                {
                    try
                    {
                        tokenToReturn = await Facebook.ExchangeTokenForLongLivedToken(accessToken);
                        await Database.UpdateUserAccessToken(userLoginId, tokenToReturn);
                    }
                    catch (Exception e)
                    {
                        // do nothing let go as exchanging token or saving it is not important to stop user login flow. it can be done 
                        // at a later login.
                    }
                }

                return LoginResult.CreateSuccess(new
                {
                    newAccessToken = tokenToReturn == accessToken ? null : tokenToReturn,
                    userLoginId = loginContext.GenerateAppScopedLoginId(),         
                    sfsToken = sfsToken
                });
            }
            catch (Exception e)
            {
                return LoginResult.CreateFailed("Oops! Please try signing in again.");
            }
        }

        private static void EnsureAccessTokenNotEmpty(string accessToken)
        {
            if (string.IsNullOrEmpty(accessToken))
            {
                throw new Exception("Got empty access token. Please try again.");
            }
        }
    }
}
