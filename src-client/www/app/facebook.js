define(function (require) {
    var globals = require('app/globals');
    var errors = require('app/errors');
    var permissions = ["email", "public_profile", "user_location"];

    if (window.cordova.platformId == "browser") {
        facebookConnectPlugin.browserInit(globals.facebookAppId);
        permissions.push("rerequest"); // this is required for browser platform alone for permission rerequest to occur :/
    }

    var login = function () {
        return new Promise(function (resolve, reject) {
            facebookConnectPlugin.login(permissions, function onResponse(response) {
                if (response.status === 'connected') {
                    resolve({
                        userId: response.authResponse.userID,
                        accessToken: response.authResponse.accessToken
                    });
                }
                else {
                    reject(getFacebookLoginError());
                }
            }, function onError(response) {
                reject(getFacebookLoginError())
            });
        });
    };

    function getFacebookLoginError(){
        return new errors.AppError(errors.errorTypes.FACEBOOK_LOGIN_ERROR);
    }

    var loginStatus = function () {
        return new Promise(function (resolve, reject) {
            facebookConnectPlugin.getLoginStatus(function onResponse(response) {
                if (response.status === 'connected') {
                    resolve({
                        userId: response.authResponse.userID,
                        accessToken: response.authResponse.accessToken
                    });
                }
                else {
                    reject(getFacebookLoginError());
                }
            }, function onError(response) {
                reject(getFacebookLoginError());
            });
        });
    };


    return {
        loginStatus: loginStatus,
        login: login
    }
});

