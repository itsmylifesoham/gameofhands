define(function(require){

    var errorTypes = {
        INTERNET_DISCONNECTED: "INTERNET_DISCONNECTED",
        FACEBOOK_LOGIN_ERROR: "FACEBOOK_LOGIN_ERROR",
        WEBSITE_LOGIN_ERROR: "WEBSITE_LOGIN_ERROR",
        SFS_CONNECTION_ERROR: "SFS_CONNECTION_ERROR",
        SFS_LOGIN_ERROR: "SFS_LOGIN_ERROR",
    };

    var AppError = function(errorType, data){
        this.errorType = errorType;
        this.data = data;
    }

    AppError.prototype.displayMessage = function(){
        if (this.errorType === errorTypes.INTERNET_DISCONNECTED)
            return "Please check internet connectivity.";
        else if(this.errorType === errorTypes.FACEBOOK_LOGIN_ERROR)
            return "Problem logging into facebook.";
        else if(this.errorType === errorTypes.WEBSITE_LOGIN_ERROR)
            return "Problem while logging into app";
        else if(this.errorType === errorTypes.SFS_CONNECTION_ERROR)
            return "Problem connecting game server.";
        else if(this.errorType === errorTypes.SFS_LOGIN_ERROR)
            return "Problem logging into game.";

        return "";
    }

    return {
        AppError: AppError,
        errorTypes: errorTypes,
    }


});