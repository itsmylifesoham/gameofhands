using System;
using System.Threading.Tasks;
using GameOfHands.Web.Models.Login;
using GameOfHands.Web.Services;

namespace GameOfHands.Web.ApplicationServices
{
    public class GuestLoginService
    {
        public static async Task<LoginResult> Login(string requestUserHostAddress, LoginContext loginContext)
        {
            try
            {
                var userLoginId = loginContext.GenerateAppScopedLoginId();

                var sessionToken = await Database.CreateGuestSfsSession(loginContext, requestUserHostAddress);
                
                return LoginResult.CreateSuccess(new
                {
                    userLoginId = loginContext.GenerateAppScopedLoginId(),
                    sessionToken = sessionToken
                });
            }
            catch (Exception e)
            {
                return LoginResult.CreateFailed("Oops! Please try signing in again.");
            }
        }
    }
}