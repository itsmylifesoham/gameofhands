using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GameOfHands.Web.Models.Facebook
{
    public class Error
    {
        public int code { get; set; }
        public string message { get; set; }
        public int subcode { get; set; }
    }

    public class DebugTokenData
    {
        public string app_id { get; set; }
        public string type { get; set; }
        public string application { get; set; }
        public Error error { get; set; }
        public int expires_at { get; set; }
        public bool is_valid { get; set; }
        public List<string> scopes { get; set; }
        public string user_id { get; set; }
    }

    public class DebugTokenDataRoot
    {
        public DebugTokenData data { get; set; }
    }
}
