using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GameOfHands.Web.Models.Facebook
{

    public static class FacebookLoginConfiguration
    {
        public static string[] MandatoryPermissions = new string[]{ "email", "public_profile"};
    }

    

    public class ExchangeTokenData
    {
        public string access_token { get; set; }
        public string token_type { get; set; }
        public int expires_in { get; set; }
    }
}