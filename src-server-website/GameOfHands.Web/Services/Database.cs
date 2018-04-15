using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web.Configuration;
using GameOfHands.Web.Models.Login;
using GameOfHands.Web.Models.User;
using MySql.Data.MySqlClient;


namespace GameOfHands.Web.Services
{
    public class Database
    {
        private static MySqlConnection GetMysqlConnection()
        {
            var connection = new MySqlConnection(WebConfigurationManager.ConnectionStrings["mysqlServerConnection"].ConnectionString);
            connection.Open();
            return connection;
        }
        public static async Task<User> GetUser(string userLoginId)
        {
            using (var connection = GetMysqlConnection())
            {
                string stm = "SELECT * FROM users WHERE user_login_id = @userLoginId";
                MySqlCommand cmd = new MySqlCommand(stm, connection);
                cmd.Parameters.AddWithValue("@userLoginId", userLoginId);
                var reader = cmd.ExecuteReader();
                bool foundUser = await reader.ReadAsync();

                if (!foundUser)
                    return null;

                return User.CreateUserFromReadRow(reader);
            }
        }

        public static async Task UpdateBasicUserInfo(BasicUserInfo basicUserInfo, string userLoginId,  string userAccessToken, bool isNewUser)
        {
            using (var connection = GetMysqlConnection())
            {
                string cmdText = null;
                if (isNewUser)
                {
                    cmdText =
                        $"INSERT INTO users (user_login_id, country, email_id, profile_pic_url, display_name, user_access_token, info_updated_date) VALUES (@userLoginId, @country, @emailId, @profilePicUrl, @displayName, @userAccessToken, @infoUpdatedDate)";
                }
                else
                {
                    cmdText = $"UPDATE users SET country=@country, email_id=@emailId, profile_pic_url=@profilePicUrl, display_name=@displayName, user_access_token=@userAccessToken, info_updated_date=@info_updated_date WHERE user_login_id = @userLoginId";
                }

                var cmd = new MySqlCommand(cmdText, connection);
                cmd.Parameters.AddWithValue("@userLoginId", userLoginId);
                cmd.Parameters.AddWithValue("@country", basicUserInfo.Country);
                cmd.Parameters.AddWithValue("@emailId", basicUserInfo.EmailId);
                cmd.Parameters.AddWithValue("@profilePicUrl", basicUserInfo.ProfilePicUrl);
                cmd.Parameters.AddWithValue("@displayName", basicUserInfo.DisplayName);
                cmd.Parameters.AddWithValue("@userAccessToken", userAccessToken);
                cmd.Parameters.AddWithValue("@infoUpdatedDate", Utilities.GetSQLFormattedDateTime(DateTime.Now));
               

                var affectedRows = await cmd.ExecuteNonQueryAsync();
                if (affectedRows == 0)
                {
                    throw new Exception("Could not update user's basic info in database.");
                }
            }
        }

        public static async Task UpdateUserAccessToken(string userLoginId, string userAccessToken)
        {
            using (var connection = GetMysqlConnection())
            {
                string cmdText =
                    $"UPDATE users SET user_access_token=@userAccessToken WHERE user_login_id=@userLoginId";
                var cmd = new MySqlCommand(cmdText, connection);
                cmd.Parameters.AddWithValue("@userLoginId", userLoginId);
                cmd.Parameters.AddWithValue("@userAccessToken", userAccessToken);

                var affectedRows = await cmd.ExecuteNonQueryAsync();
                if (affectedRows == 0)
                {
                    throw new Exception("Could not update user's access token.");
                }
            }
        }

        public static async Task<string> CreateSfsSession(LoginContext loginContext, string ipAddress, string userAccessToken)
        {
            using (var connection = GetMysqlConnection())
            {
                var userLoginId = loginContext.GenerateAppScopedLoginId();
                User matchedExistingUser = await GetUser(userLoginId);

                if (matchedExistingUser == null || !matchedExistingUser.BasicUserInfo.IsRecent())
                {
                    var userInfoFacebook = await Facebook.GetUserProfileInfo(loginContext.UserId, userAccessToken);
                    var basicUserInfo = BasicUserInfo.CreateFromUserProfileInfo(userInfoFacebook);
                    await UpdateBasicUserInfo(basicUserInfo, userLoginId, userAccessToken, matchedExistingUser==null);
                }

                string cmdText = $"INSERT INTO user_sessions (user_login_id, session_token, ip_address, date_created) VALUES (@userLoginId, @sessionToken, @ipAddress,  @dateCreated)";
                var cmd = new MySqlCommand(cmdText, connection);
                cmd.Parameters.AddWithValue("@userLoginId", userLoginId);
                var sessionToken = Guid.NewGuid().ToString();
                cmd.Parameters.AddWithValue("@sessionToken", sessionToken);
                cmd.Parameters.AddWithValue("@ipAddress", ipAddress);
                cmd.Parameters.AddWithValue("@dateCreated", Utilities.GetSQLFormattedDateTime(DateTime.Now));
                

                var affectedRows = await cmd.ExecuteNonQueryAsync();
                if (affectedRows == 0)
                {
                    throw new Exception("Could not create a game session. Please try again.");
                }

                return sessionToken;
            }
        }

        
    }
}
