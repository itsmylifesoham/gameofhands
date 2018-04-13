using GameOfHands.Web.Models.Website;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

namespace GameOfHands.Web.Models.Login
{
    public enum LoginStatus
    {
        fail,
        success
    }
    public class LoginResult
    {
        [JsonConverter(typeof(StringEnumConverter))]
        public LoginStatus LoginStatus { get; private set; }

        public object Payload { get; private set; }

        public LoginResult(LoginStatus loginStatus, object payload = null)
        {
            this.LoginStatus = loginStatus;
            this.Payload = payload;
        }

        public string ToJson()
        {
            return JsonConvert.SerializeObject(this, AppConfiguration.jsonSerializationSettings);
        }

        public static LoginResult CreateFailed(string message)
        {
            return new LoginResult(Login.LoginStatus.fail, new
            {
                error = message
            } );
        }

        public static LoginResult CreateSuccess(object payload)
        {
            return new LoginResult(Login.LoginStatus.success, payload);
        }
    }
}