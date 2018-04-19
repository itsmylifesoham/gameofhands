define(function (require) {

    var helpers = require('app/helpers');
    var errors = require('app/errors');

    SFS2X.SmartFox.extend = helpers.extend;


    var SfsExtended = SFS2X.SmartFox.extend({
        connect: function () {

            var sfsInstance = this;
            if (sfsInstance.isConnected)
                return Promise.resolve("already connected to sfs!");

            return new Promise(function (resolve, reject) {
                sfsInstance.addEventListener(SFS2X.SFSEvent.CONNECTION, onConnection, sfsInstance);

                function onConnection(evtParams) {
                    if (evtParams.success)
                        resolve("connected to sfs!");
                    else
                        reject(new errors.AppError(errors.errorTypes.SFS_CONNECTION_ERROR));

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
            return this.logout()
                .then(function () {
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
                            reject(new errors.AppError(errors.errorTypes.SFS_LOGIN_ERROR, evtParams.errorMessage));
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
                })
                .catch(function () {
                    return Promise.reject(new errors.AppError(errors.errorTypes.SFS_LOGIN_ERROR));
                });
        },


    });

    return {
        SmartFox: SfsExtended
    }
});