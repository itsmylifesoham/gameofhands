define(function (require) {
    var globals = require('app/globals');

    var getLoggedInUser = function (success, error) {
        facebookConnectPlugin.getLoginStatus(
            function onResponse(response) {
                if (response.status === 'connected') {
                    success({
                        userId: response.authResponse.userID,
                        accessToken: response.authResponse.accessToken
                    });
                }
                else {
                    error();
                }
            }, function onFailure() {
                error();
            });
    };

    var login = function (success, error) {
        if (window.cordova.platformId == "browser") {
            facebookConnectPlugin.browserInit(globals.facebookAppId);
        }
        facebookConnectPlugin.login(["email", "public_profile"],
            function onResponse(response) {
                if (response.status === 'connected') {
                    success({
                        userId: response.authResponse.userID,
                        accessToken: response.authResponse.accessToken
                    });
                }
                else {
                    error();
                }
            },
            error);
    }

    return {
        getLoggedInUser: getLoggedInUser,
        login: login
    }

});

