define(function (require) {


    var errors = require('app/errors');
    SFS2X.SmartFox.extend = Backbone.View.extend;

    var _getSfsLoginPromise = function (sfsInstance, userLoginId, sessionToken) {
        return new Promise(function (resolve, reject) {
            sfsInstance.addEventListener(SFS2X.SFSEvent.LOGIN, onLogin, sfsInstance);
            sfsInstance.addEventListener(SFS2X.SFSEvent.LOGIN_ERROR, onLoginError, sfsInstance);

            function onLogin(evtParams) {
                evtParams.user.loginData = evtParams.data;
                resolve({
                    user: evtParams.user
                });
                removeSfsEventHandlers();
            }

            function onLoginError(evtParams) {
                reject(new errors.SFSLoginError(evtParams.errorMessage));
                removeSfsEventHandlers();
            }

            function removeSfsEventHandlers() {
                sfsInstance.removeEventListener(SFS2X.SFSEvent.LOGIN, onLogin);
                sfsInstance.removeEventListener(SFS2X.SFSEvent.LOGIN_ERROR, onLoginError);
            }

            var loginData = new SFS2X.SFSObject();
            loginData.putUtfString('sessionToken', sessionToken);

            sfsInstance.send(new SFS2X.LoginRequest(userLoginId, "", loginData));
        });
    };

    var SfsExtended = SFS2X.SmartFox.extend({
        connect: function () {
            var sfsInstance = this;
            if (sfsInstance.isConnected) {
                return Promise.resolve("already connected!");
            }

            return new Promise(function (resolve, reject) {

                sfsInstance.addEventListener(SFS2X.SFSEvent.CONNECTION, onConnection, sfsInstance);

                function onConnection(evtParams) {
                    if (evtParams.success)
                        resolve("connected to sfs!");
                    else
                        reject(new errors.SFSConnectionError("Could not connect to game server."));

                    removeSfsEventHandlers();
                };

                function removeSfsEventHandlers() {
                    sfsInstance.removeEventListener(SFS2X.SFSEvent.CONNECTION, onConnection);
                }

                SFS2X.SmartFox.prototype.connect.call(sfsInstance);
            });
        },
        logout: function () {
            var sfsInstance = this;
            return new Promise(function (resolve, reject) {
                if (sfsInstance.mySelf) {
                    var onLogout = function () {
                        resolve();
                        sfsInstance.removeEventListener(SFS2X.SFSEvent.LOGOUT, onLogout);
                    }

                    sfsInstance.addEventListener(SFS2X.SFSEvent.LOGOUT, onLogout, this);
                    sfsInstance.send(new SFS2X.LogoutRequest());
                }
                else {
                    resolve();
                }
            });
        },
        login: function (userLoginId, sessionToken) {
            var sfsInstance = this;
            if (sfsInstance.mySelf) {
                //logout the currently logged in user first
                return sfsInstance.logout()
                    .then(function(){
                        return _getSfsLoginPromise(sfsInstance, userLoginId, sessionToken);
                    })
                    .catch(function(){
                        return Promise.reject(new errors.SFSLoginError());
                    });
            }
            else{
                return _getSfsLoginPromise(sfsInstance, userLoginId, sessionToken);
            }
        },


    });

    return {
        SmartFox: SfsExtended
    }
});