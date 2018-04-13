using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GameOfHands.Web.Models.Website
{
    public class AppConfiguration
    {
        public static JsonSerializerSettings jsonSerializationSettings = new JsonSerializerSettings { NullValueHandling = NullValueHandling.Ignore };

    }
}