namespace GameOfHands.Web.Models.Facebook
{
    public class ProfilePicData
    {
        public string url { get; set; }
    }

    public class Picture
    {
        public ProfilePicData data { get; set; }
    }

    public class Location2
    {
        public string country { get; set; }
    }

    public class Location
    {
        public Location2 location { get; set; }
        public string id { get; set; }
    }

    public class UserProfileInfo
    {
        public string email { get; set; }
        public string first_name { get; set; }
        public Picture picture { get; set; }
        public Location location { get; set; }
        public string id { get; set; }

        public string ProfilePictureUrl => picture.data.url;

        public string Country => location.location.country;
    }
}