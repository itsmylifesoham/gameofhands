using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web.Configuration;
using GameOfHands.Web.Models.Facebook;
using GameOfHands.Web.Models.Login;

namespace GameOfHands.Web.ApplicationServices
{
    public class FacebookLoginService
    {
        public async Task<LoginResult> Login(string accessToken)
        {
            EnsureAccessTokenNotEmpty(accessToken);

            throw new NotImplementedException();
        }

        private static void EnsureAccessTokenNotEmpty(string accessToken)
        {
            if (string.IsNullOrEmpty(accessToken))
            {
                throw new Exception("Got empty access token.");
            }
        }

        private bool AreAllRequiredFacebookPermissionsPresentInAccessToken(DebugTokenData debugTokenData)
        {
            var scopesInAccessToken = new HashSet<string>(debugTokenData.scopes);
            foreach (var requiredPermission in FacebookLoginConfiguration.RequiredPermissions)
            {
                if (!scopesInAccessToken.Contains(requiredPermission))
                {
                    return false;
                }
            }

            return true;
        }

        private bool IsAppIdCorrect(DebugTokenData debugTokenData)
        {
            return debugTokenData.app_id.Equals(WebConfigurationManager.AppSettings["facebookAppId"]);
        }

        private bool TokenExpiringInLessThanOneDay(DebugTokenData debugTokenData)
        {
            var tokenExpiryDateTime = Utilities.UnixTimeStampToDateTime(debugTokenData.expires_at);
            if (tokenExpiryDateTime - DateTime.Now < TimeSpan.FromDays(1))
            {
                return true;
            }

            return false;
        }
    }
}
