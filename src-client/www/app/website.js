define(function (require) {
    var globals = require('app/globals');
    var helpers = require('app/helpers');
    var websiteRequestTimeout = 10000;

    var assignWebsiteLoginFlow = function (fetchEndpointPromise, resolve, reject) {
        fetchEndpointPromise
            .then(function (response) {
                return response.json();
            })
            .then(function (loginResultJson) {
                if (loginResultJson.LoginStatus === 'success') {
                    resolve(loginResultJson.Payload);
                }
                else {
                    reject(loginResultJson.Payload);
                }
            }).catch(function () {
            reject("oops! problem logging in. try again.");
        });
    };

    var websiteEndpoints = {
        getFbLoginEndpoint: function (userId, accessToken) {
            return globals.websiteEndPoint + 'login/facebook?user_id=' + userId + '&access_token=' + accessToken + '&app_name=' + globals.appName;
        },
        getGuestLoginEndpoint: function () {
            return globals.websiteEndPoint + 'login/guest?app_name=' + globals.appName;
        }
    };

    var loginFb = function (userId, accessToken) {
        return new Promise(function (resolve, reject) {
            var fetchEndpointPromise = helpers.fetchTimeOut(websiteRequestTimeout, websiteEndpoints.getFbLoginEndpoint(userId, accessToken));
            assignWebsiteLoginFlow(fetchEndpointPromise, resolve, reject);
        });
    };


    var loginGuest = function () {
        return new Promise(function (resolve, reject) {
            var fetchEndpointPromise = helpers.fetchTimeOut(websiteRequestTimeout, websiteEndpoints.getGuestLoginEndpoint());
            assignWebsiteLoginFlow(fetchEndpointPromise, resolve, reject);
        });
    };




    return {
        loginFb: loginFb,
        loginGuest: loginGuest,
    };

});