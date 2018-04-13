using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GameOfHands.Web.Models.Login
{
    public class LoginIdGenerator
    {
        public static string GetLoginId(string userId, LoginType loginType)
        {
            if (loginType == LoginType.Facebook)
            {
                return $"facebook:{userId}";
            }
            
            throw new Exception("This login type is not supported");
        }
    }
}