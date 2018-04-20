define(function (require) {
    var facebook = require('app/facebook');
    var website = require('app/website');
    var globals = require('app/globals');
    var internet = require('app/internet');
    var helpers = require('app/helpers');
    var loginTimeout = 10000;

    var connectToFbByDialog = function () {
        return internet.connected()
            .then(function () {
                return facebook.login();
            })
            .then(function (userData) {
                return helpers.timeoutPromise(loginTimeout, assignSfsFlow(website.loginFb(userData.userId, userData.accessToken)));
            })
            .catch(function(error){
                return Promise.reject(error);
            });
    };


    var connectToFbAutomatically = function () {
        return helpers.timeoutPromise(loginTimeout, assignSfsFlow(internet.connected()
            .then(function () {
                return facebook.loginStatus();
            })
            .then(function (userData) {
                return website.loginFb(userData.userId, userData.accessToken);
            })));

    };

    var connectAsGuest = function () {
        return helpers.timeoutPromise(loginTimeout,assignSfsFlow(internet.connected()
            .then(function () {
                return website.loginGuest();
            })));
    };

    function assignSfsFlow(websiteLoginChain) {
        return websiteLoginChain
            .then(function (loginResultPayload) {
                return globals.app.sfs.connect()
                    .then(function () {
                        return globals.app.sfs.login(loginResultPayload.userLoginId, loginResultPayload.sessionToken);
                    })
                    .then(function (evtParams) {
                        return Promise.resolve(evtParams)
                    })
                    .catch(function (error) {
                        return Promise.reject(error);
                    });
            })
            .catch(function (error) {
                return Promise.reject(error);
            });
    }

    return {
        connectToFbByDialog: connectToFbByDialog,
        connectToFbAutomatically: connectToFbAutomatically,
        connectAsGuest: connectAsGuest,
    }
});