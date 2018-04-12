using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GameOfHands.Web.ApplicationServices
{
    public enum LoginType
    {
        Facebook
    }
    public class UserLoginIdService
    {
        public static string GetUserLoginId(string userId, LoginType loginType)
        {
            return $"{loginType.ToString()}:{userId}";
        }
    }
}
