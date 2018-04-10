using MySql.Data.MySqlClient;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GameOfHands.Web.Models.User
{
    public class BasicUserInfo
    {
        public string profile_pic { get; set; }
        public string country { get; set; }
        public string display_name { get; set; }
        public string last_updated_date_string { get; set; }

        public DateTime LastUpdated()
        {
            return Convert.ToDateTime(this.last_updated_date_string);
        }

        public bool IsRecent()
        {
            return DateTime.Now - LastUpdated() < TimeSpan.FromDays(1);
        }
    }

    public class User
    {
        public int Id { get; set; }
        public string UserId { get; set; }

        public BasicUserInfo BasicUserInfo { get; set; }

        public static User CreateUserFromReadRow(MySqlDataReader reader)
        {
            return new User
            {
                Id = reader.GetInt32("id"),
                UserId = reader.GetString("user_login_id"),
                BasicUserInfo = JsonConvert.DeserializeObject<BasicUserInfo>(reader.GetString("basic_user_info"))
            };
        }   

      
    }
}