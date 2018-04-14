define(function (require) {
    var globals = require('app/globals');

    var login = function (userId, accessToken) {
        return new Promise(function(resolve, reject){
            fetch(globals.websiteEndPoint + 'login/facebook?user_id=' + userId + '&access_token=' + accessToken + '&app_name=' + globals.appName )
                .then(function(response) {
                    return response.json();
                })
                .then(function(loginResultJson) {
                    if(loginResultJson.LoginStatus === 'success'){
                        resolve(loginResultJson.Payload);
                    }
                    else{
                        reject(loginResultJson.Payload);
                    }
                }).catch(function(){
                    reject("oops! problem logging in. try again.");
            });
        });
    };

    return {
        login: login,
    };

});