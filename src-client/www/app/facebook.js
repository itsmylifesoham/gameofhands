define(function (require) {
    var globals = require('app/globals');

    if (window.cordova.platformId == "browser") {
        facebookConnectPlugin.browserInit(globals.facebookAppId);
    }

    var login = function () {
        return new Promise(function (resolve, reject) {
            facebookConnectPlugin.login(["email", "public_profile", "rerequest"], function onResponse(response) {
                if (response.status === 'connected') {
                    resolve({
                        userId: response.authResponse.userID,
                        accessToken: response.authResponse.accessToken
                    });
                }
                else {
                    reject(response);
                }
            }, function onError(response) {
                reject(response)
            });
        });
    };


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
                    reject(response);
                }
            }, function onError(response) {
                reject(response);
            });
        });
    };


    return {
        loginStatus: loginStatus,
        login: login
    }
});

