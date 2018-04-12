using GameOfHands.Web.Models.Facebook;
using GameOfHands.Web.Models.User;
using MySql.Data.MySqlClient;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web;
using System.Web.Configuration;
using System.Web.Mvc;
using GameOfHands.Web.ApplicationServices;
using GameOfHands.Web.Models.Login;

namespace GameOfHands.Web.Controllers
{
    public class LoginController : Controller
    {
        private readonly HttpClient _httpClient;

        public LoginController()
        {
            _httpClient = HttpClientPool.GetHttpClient();
        }

        public async Task<JsonResult> Facebook(string access_token)
        {
            try
            {
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


                CreateNewSessionForUser(FacebookLoginService.GetUserLoginIdFromFacebookUserId(debugTokenData.user_id), newToken);
                return GetSuccessJsonResult(newToken);
            }
            catch (Exception exp)
            {
                return GetErrorJsonResult("Server Error occured while logging in. Please try again in some time.");
            }


        }

        private string GetSQLFormattedDateTime(DateTime dateTime)
        {
            return dateTime.ToString("yyyy-MM-dd HH:mm:ss.fff");
        }

        private void CreateNewSessionForUser(string userLoginId, string newToken)
        {
            var connection = new MySqlConnection(WebConfigurationManager.ConnectionStrings["testMysqlServer"].ConnectionString);
            connection.Open();

            User matchedExistingUser = GetUser(userLoginId, connection);

            if (matchedExistingUser != null && !matchedExistingUser.BasicUserInfo.IsRecent())
            {
                UpdateUserBasicInfo(userLoginId, newToken, connection);
            }
            
            AddNewSession(userLoginId, connection);
            connection.Close();
        }

        private void UpdateUserBasicInfo(string userLoginId, string newToken, MySqlConnection connection)
        {
            throw new NotImplementedException();
        }

        private User GetUser(string userLoginId, MySqlConnection connection)
        {           
            string stm = "SELECT * FROM users WHERE user_login_id = @userLoginId";
            MySqlCommand cmd = new MySqlCommand(stm, connection);
            cmd.Parameters.AddWithValue("@userLoginId", userLoginId);
            
            var reader = cmd.ExecuteReader();
            Models.User.User matchedExistingUser = null;

            while (reader.Read())
            {
                matchedExistingUser = Models.User.User.CreateUserFromReadRow(reader);
                break;
            }

            reader.Close();
            cmd.Dispose();
            return matchedExistingUser;
        }

        private void AddNewSession(string userLoginId, MySqlConnection connection)
        {
            string cmdText = $"INSERT INTO user_sessions (user_login_id, consume_once_token, token_consumed, ip_address, date_created) VALUES ('{userLoginId}', '{Guid.NewGuid().ToString()}', 0 , '{Request.UserHostAddress}', '{GetSQLFormattedDateTime(DateTime.Now)}')";
            var cmd = new MySqlCommand(cmdText, connection);            
            //cmd.Parameters.AddWithValue("@user_login_id",userLoginId);
            //cmd.Parameters.AddWithValue("@consume_once_token", Guid.NewGuid().ToString());
            //cmd.Parameters.AddWithValue("@token_consumed", "0");
            //cmd.Parameters.AddWithValue("@ip_address", Request.UserHostAddress);
            //cmd.Parameters.AddWithValue("@date_created", DateTime.Now.ToString());

            cmd.ExecuteNonQuery();
            cmd.Dispose();
        }


        

        
    }
}