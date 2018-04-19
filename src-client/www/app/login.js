define(function(require){
    var facebook = require('app/facebook');
    var website = require('app/website');
    var globals = require('app/globals');

    var connectToFbByDialog = function(){
        return assignWebsiteFbLoginFlow(facebook.login());
    };

    var connectToFbAutomatically = function(){
        return assignWebsiteFbLoginFlow(facebook.loginStatus());
    };

    var connectAsGuest = function(){
        return assignSfsLoginFlow(website.loginGuest());
    }

    function assignWebsiteFbLoginFlow(fbLoginPromise) {

        var websiteLoginPromise = fbLoginPromise
            .then(function (userData) {
                return website.loginFb(userData.userId, userData.accessToken);
            });

        return assignSfsLoginFlow(websiteLoginPromise);

    };

    function assignSfsLoginFlow(websiteLoginPromise) {
        return websiteLoginPromise
            .then(function (loginResultPayload) {
                return globals.app.sfs.connect()
                    .then(function (msg) {
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
    };

    return {
        connectToFbByDialog: connectToFbByDialog,
        connectToFbAutomatically: connectToFbAutomatically,
        connectAsGuest: connectAsGuest,
    }
});