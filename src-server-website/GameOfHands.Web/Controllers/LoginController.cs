using GameOfHands.Web.Models.facebook;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web;
using System.Web.Configuration;
using System.Web.Mvc;

namespace GameOfHands.Web.Controllers
{
    public static class LoginStatus
    {
        public static string Fail = "fail";
        public static string Success = "success";
    }

    public class LoginController : Controller
    {
        private static HttpClient httpClient = new HttpClient();

        private static string FacebookApiEndpoint = "https://graph.facebook.com/v2.12";

        public async Task<JsonResult> Facebook(string access_token)
        {
            try
            {
                if (string.IsNullOrEmpty(access_token))
                {
                    return GetErrorJsonResult("No Facebook Access token provided");
                }

                var debugTokenData = await GetFacebookAccessTokenDebugInfo(access_token);
                if (debugTokenData == null)
                {
                    return GetErrorJsonResult("Unable to contact facebook servers to verify token. Please try again in some time.");
                }

                if (debugTokenData.error != null || !debugTokenData.is_valid)
                {
                    GetErrorJsonResult(debugTokenData.error.message);
                }

                if (!IsAppIdCorrect(debugTokenData))
                    return GetErrorJsonResult("This access token does not belong to our app. Please log in again.");

                // check permissions are fine here
                if (!AreAllRequiredFacebookPermissionsPresentInAccessToken(debugTokenData))
                    return GetErrorJsonResult("Facebook permissions missing. Please log in again and allow required permissions.");

                string newToken = access_token;
                if (TokenExpiringInLessThanOneDay(debugTokenData))
                {
                    newToken = await ExchangeTokenForLongLivedToken(access_token);
                    if (newToken == null)
                    {
                        return GetErrorJsonResult("Unable to extend expiring access token.");
                    }
                }

                return GetSuccessJsonResult(newToken);
            }
            catch (Exception)
            {
                return GetErrorJsonResult("Server Error occured while logging in. Please try again in some time.");
            }

            //PersistDataForFacebookUser(debugTokenData.user_id, newToken);

        }

        private JsonResult GetSuccessJsonResult(string newToken)
        {
            return Json(new
            {
                status = LoginStatus.Success,
                newToken = newToken
            }, JsonRequestBehavior.AllowGet);
        }

        private async Task<string> ExchangeTokenForLongLivedToken(string access_token)
        {
            var response = await httpClient.GetAsync($"{FacebookApiEndpoint}/oauth/access_token?access_token={WebConfigurationManager.AppSettings["facebookAppAccessToken"]}&grant_type=fb_exchange_token&client_id={WebConfigurationManager.AppSettings["facebookAppId"]}&client_secret={WebConfigurationManager.AppSettings["facebookAppSecret"]}&fb_exchange_token={access_token}");

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

        private bool TokenExpiringInLessThanOneDay(DebugTokenData debugTokenData)
        {
            var tokenExpiryDateTime = UnixTimeStampToDateTime(debugTokenData.expires_at);
            if (tokenExpiryDateTime - DateTime.Now < TimeSpan.FromDays(1))
            {
                return true;
            }

            return false;
        }

        private DateTime UnixTimeStampToDateTime(double unixTimeStamp)
        {
            // Unix timestamp is seconds past epoch
            DateTime dtDateTime = new DateTime(1970, 1, 1, 0, 0, 0, 0, System.DateTimeKind.Utc);
            dtDateTime = dtDateTime.AddSeconds(unixTimeStamp).ToLocalTime();
            return dtDateTime;
        }

        private bool IsAppIdCorrect(DebugTokenData debugTokenData)
        {
            return debugTokenData.app_id.Equals(WebConfigurationManager.AppSettings["facebookAppId"]);
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

       

        private JsonResult GetErrorJsonResult(string errorMessage)
        {
            return Json(new
            {
                status = LoginStatus.Fail,
                error = new Error() { message = errorMessage }
            }, JsonRequestBehavior.AllowGet);
        }

        private async Task<DebugTokenData> GetFacebookAccessTokenDebugInfo(string inputToken)
        {
            var response = await httpClient.GetAsync($"{FacebookApiEndpoint}/debug_token?access_token={WebConfigurationManager.AppSettings["facebookAppAccessToken"]}&input_token={inputToken}");

            if (response.IsSuccessStatusCode)
            {
                var responseJson = await response.Content.ReadAsStringAsync();
                var debugTokenData = JsonConvert.DeserializeObject<DebugTokenDataRoot>(responseJson).data;
                return debugTokenData;
            }
            else
            {
                return null;
            }
        }
    }
}