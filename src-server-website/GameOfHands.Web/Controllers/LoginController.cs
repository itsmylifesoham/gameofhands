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
using GameOfHands.Web.Services;

namespace GameOfHands.Web.Controllers
{
    public class LoginController : Controller
    {
        public async Task<ContentResult> Facebook(string access_token, string user_id, AppName app_name)
        {
            var loginResult = await FacebookLoginService.Login(access_token, GetRequestIpAddress(), new LoginContext()
            {

                AppName = app_name,
                LoginType = LoginType.fb,
                UserId = user_id,
            });
            return Content(loginResult.ToJson(), "application/json");
        }

        public async Task<ContentResult> Guest(AppName app_name)
        {
            var loginResult = await GuestLoginService.Login(GetRequestIpAddress(), new LoginContext()
            {
                AppName = app_name,
                LoginType = LoginType.guest,
                UserId = Guid.NewGuid().ToString(),
            });
            return Content(loginResult.ToJson(), "application/json");
        }

        private string GetRequestIpAddress()
        {
            return Request.UserHostAddress == "::1" ? "127.0.0.1" : Request.UserHostAddress;
        }

    }
}