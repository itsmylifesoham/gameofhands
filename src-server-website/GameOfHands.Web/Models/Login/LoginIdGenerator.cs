using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GameOfHands.Web.Models.Login
{
    public class LoginIdGenerator
    {
        public static string GetFacebookLoginId(string userId)
        {
            return $"facebook:{userId}";
        }
    }
}