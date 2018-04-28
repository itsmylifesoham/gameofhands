define(function(require){

    var errorTypes = {
        INTERNET_DISCONNECTED: "INTERNET_DISCONNECTED",
        FACEBOOK_LOGIN_ERROR: "FACEBOOK_LOGIN_ERROR",
        WEBSITE_LOGIN_ERROR: "WEBSITE_LOGIN_ERROR",
        SFS_CONNECTION_ERROR: "SFS_CONNECTION_ERROR",
        SFS_LOGIN_ERROR: "SFS_LOGIN_ERROR",
        REQUEST_TIMEOUT_ERROR: "REQUEST_TIMEOUT_ERROR",
        UNKNOWN_ERROR: "UNKNOWN_ERROR",
        UNABLE_TO_JOIN: "UNABLE_TO_JOIN",
    };

    var AppError = function(errorType, displayMessage, data){
        this.errorType = errorType;
        this.displayMessage = displayMessage;
        this.data = data;
    }

    // using backbones extend method to avoid circular reference with helpers module
    AppError.extend = Backbone.View.extend;

    var UnknownError = AppError.extend({
        constructor: function(data){
            AppError.call(this, errorTypes.UNKNOWN_ERROR, "An unknown error occured. Lets reload the game.", data);
        }
    });

    var InternetDisconnectedError = AppError.extend({
        constructor: function(data){
            AppError.call(this, errorTypes.INTERNET_DISCONNECTED, "Please check internet connectivity.", data);
        }
    });

    var FacebookLoginError = AppError.extend({
        constructor: function(data){
            AppError.call(this, errorTypes.FACEBOOK_LOGIN_ERROR, "Problem logging into facebook.", data);
        }
    });

    var WebsiteLoginError = AppError.extend({
        constructor: function(data){
            AppError.call(this, errorTypes.WEBSITE_LOGIN_ERROR, "Problem while logging into app. Please try again.", data);
        }
    });

    var SFSConnectionError = AppError.extend({
        constructor: function(data){
            var reason = data;
            AppError.call(this, errorTypes.SFS_CONNECTION_ERROR, "Problem connecting game server." + reason? reason: "", data);
        }
    });

    var SFSLoginError = AppError.extend({
        constructor: function(data){
            AppError.call(this, errorTypes.SFS_LOGIN_ERROR, "Problem logging into game.", data);
        }
    });

    var RequestTimeoutError = AppError.extend({
        constructor: function(data){
            AppError.call(this, errorTypes.REQUEST_TIMEOUT_ERROR, "Request took too long to complete. Please check internet connectivity.", data);
        }
    });

    var UnableToJoinError = AppError.extend({
        constructor: function(data){
            AppError.call(this, errorTypes.UNABLE_TO_JOIN, "Unable to join a game. Lets try reconnecting again.", data);
        }
    });


    return {
        UnknownError: UnknownError,
        InternetDisconnectedError: InternetDisconnectedError,
        FacebookLoginError: FacebookLoginError,
        WebsiteLoginError: WebsiteLoginError,
        SFSConnectionError: SFSConnectionError,
        SFSLoginError: SFSLoginError,
        RequestTimeoutError: RequestTimeoutError,
        UnableToJoinError: UnableToJoinError,
        errorTypes: errorTypes,
    }


});