using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GameOfHands.Web.Models.Login
{
    public class LoginSource
    {
        public AppName AppName { get; set; }

        public LoginType LoginType { get; set; }

        public string UserId { get; set; }

        public string GenerateAppScopedLoginId()
        {
            return $"{AppName.ToString()}:{LoginType.ToString()}:{UserId}";
        }

        
    }
}