using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web.Configuration;

namespace GameOfHands.Web.Models.Facebook
{
    public class Error
    {
        public int code { get; set; }
        public string message { get; set; }
        public int subcode { get; set; }
    }

    public class DebugTokenData
    {
        public string app_id { get; set; }
        public string type { get; set; }
        public string application { get; set; }
        public Error error { get; set; }
        public int expires_at { get; set; }
        public bool is_valid { get; set; }
        public List<string> scopes { get; set; }
        public string user_id { get; set; }

        public bool IsValid()
        {
            return this.is_valid && this.error == null && IsAppIdCorrect() && AreAllRequiredFacebookPermissionsPresentInAccessToken();
        }

        public string GetErrorMessage()
        {
            return error != null ? error.message : string.Empty;
        }

        private bool IsAppIdCorrect()
        {
            return this.app_id.Equals(WebConfigurationManager.AppSettings["facebookAppId"]);
        }

        public bool IsTokenExpiringInLessThanOneDay()
        {
            var tokenExpiryDateTime = Utilities.UnixTimeStampToDateTime(this.expires_at);
            return tokenExpiryDateTime - DateTime.Now < TimeSpan.FromDays(1);           
        }

        private bool AreAllRequiredFacebookPermissionsPresentInAccessToken()
        {
            var scopesInAccessToken = new HashSet<string>(this.scopes);
            foreach (var requiredPermission in FacebookLoginConfiguration.RequiredPermissions)
            {
                if (!scopesInAccessToken.Contains(requiredPermission))
                {
                    return false;
                }
            }

            return true;
        }
    }

    public class DebugTokenDataRoot
    {
        public DebugTokenData data { get; set; }
    }
}
