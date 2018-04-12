﻿using GameOfHands.Web.Models.Facebook;
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
        public async Task<ContentResult> Facebook(string access_token, string user_id, string device_id)
        {
            var loginResult = await FacebookLoginService.Login(access_token, user_id, device_id, Request.UserHostAddress);
            return Content(loginResult.ToJson(), "application/json");
        }
       
    }
}