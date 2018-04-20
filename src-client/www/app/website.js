define(function (require) {
    var globals = require('app/globals');
    var errors = require('app/errors');


    var assignWebsiteLoginFlow = function (fetchEndpointPromise) {
        return fetchEndpointPromise
            .then(function (response) {
                return response.json();
            })
            .then(function (loginResultJson) {
                if (loginResultJson.LoginStatus === 'success') {
                    return Promise.resolve(loginResultJson.Payload);
                }
                else {
                    return Promise.reject(getWebsiteLoginError(loginResultJson.Payload));
                }
            })
            .catch(function () {
                return Promise.reject(getWebsiteLoginError());
            });
    };

    function getWebsiteLoginError(data) {
        return new errors.WebsiteLoginError(data);
    }

    var websiteEndpoints = {
        getFbLoginEndpoint: function (userId, accessToken) {
            return globals.websiteEndPoint + 'login/facebook?user_id=' + userId + '&access_token=' + accessToken + '&app_name=' + globals.appName;
        },
        getGuestLoginEndpoint: function () {
            return globals.websiteEndPoint + 'login/guest?app_name=' + globals.appName;
        }
    };

    var loginFb = function (userId, accessToken) {
        return assignWebsiteLoginFlow(fetch( websiteEndpoints.getFbLoginEndpoint(userId, accessToken)));
    };


    var loginGuest = function () {
        return assignWebsiteLoginFlow(fetch( websiteEndpoints.getGuestLoginEndpoint()));
    };


    return {
        loginFb: loginFb,
        loginGuest: loginGuest,
    };

});