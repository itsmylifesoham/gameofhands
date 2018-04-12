using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web.Configuration;
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

        public static async Task CreateSfsSession(string userLoginId, string deviceId, string ipAddress)
        {
            using (var connection = GetMysqlConnection())
            {
                User matchedExistingUser = await GetUser(userLoginId);

                if (matchedExistingUser == null || !matchedExistingUser.BasicUserInfo.IsRecent())
                {
                    throw new NotImplementedException("implement fetching data from facebook and putting into db here");
                }

                string cmdText = $"INSERT INTO user_sessions (user_login_id, consume_once_token, token_consumed, ip_address, device_id, date_created) VALUES (@userLoginId, @consumeOnceToken, 0 , @ipAddress, @deviceId, @dateCreated)";
                var cmd = new MySqlCommand(cmdText, connection);
                cmd.Parameters.AddWithValue("@userLoginId", userLoginId);
                cmd.Parameters.AddWithValue("@consumeOnceToken", Guid.NewGuid().ToString());
                cmd.Parameters.AddWithValue("@ipAddress", ipAddress);
                cmd.Parameters.AddWithValue("@dateCreated", Utilities.GetSQLFormattedDateTime(DateTime.Now));
                cmd.Parameters.AddWithValue("@deviceId", deviceId);

                var affectedRows = cmd.ExecuteNonQuery();
                if (affectedRows == 0)
                {
                    throw new Exception("Could not create a game session.");
                }
            }
        }

        
    }
}
