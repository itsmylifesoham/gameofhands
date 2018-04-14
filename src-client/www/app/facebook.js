define(function (require) {
    var globals = require('app/globals');
    var permissions = ["email", "public_profile"];

    if (window.cordova.platformId == "browser") {
        facebookConnectPlugin.browserInit(globals.facebookAppId);
        permissions.push("rerequest");
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

