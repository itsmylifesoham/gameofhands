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
        private static TimeSpan OneDaySpan = TimeSpan.FromDays(1);
        public string Country { get; set; }

        public string EmailId { get; set; }

        public string ProfilePicUrl { get; set; }

        public string DisplayName { get; set; }

        public DateTime UserInfoUpdateDate { get; set; }

        public bool IsRecent()
        {
            return DateTime.Now - UserInfoUpdateDate < OneDaySpan;
        }

        public static BasicUserInfo CreateFromReadRow(MySqlDataReader reader)
        {
            return new BasicUserInfo()
            {
                Country = reader.GetString("country"),
                EmailId = reader.GetString("email_id"),
                ProfilePicUrl = reader.GetString("profile_pic_url"),
                DisplayName = reader.GetString("display_name"),
                UserInfoUpdateDate = reader.GetDateTime("info_updated_date")
            };
        }
    }

    public class User
    {
        public int Id { get; set; }
        public string UserLoginId { get; set; }        

        public BasicUserInfo BasicUserInfo { get; set; }

        public static User CreateUserFromReadRow(MySqlDataReader reader)
        {
            return new User
            {
                Id = reader.GetInt32("id"),
                UserLoginId = reader.GetString("user_login_id"),
                BasicUserInfo = BasicUserInfo.CreateFromReadRow(reader)
            };
        }   

      
    }
}