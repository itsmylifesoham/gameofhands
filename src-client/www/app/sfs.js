define(function (require) {

    var helpers = require('app/helpers');
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
                        reject("unable to connect to sfs");

                    removeSfsEventHandlers();
                };

                function removeSfsEventHandlers(){
                    sfsInstance.removeEventListener(SFS2X.SFSEvent.CONNECTION, onConnection);
                }

                SFS2X.SmartFox.prototype.connect.call(sfsInstance);
            });
        },
        login: function (userLoginId, sessionToken) {
            var sfsInstance = this;
            if(sfsInstance.mySelf != null)
                return Promise.resolve({
                   user: sfsInstance.mySelf
                });

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
                    reject(evtParams.errorMessage);
                    removeSfsEventHandlers();
                }

                function removeSfsEventHandlers(){
                    sfsInstance.removeEventListener(SFS2X.SFSEvent.LOGIN, onLogin);
                    sfsInstance.removeEventListener(SFS2X.SFSEvent.LOGIN_ERROR, onLoginError);
                }

                var loginData = new SFS2X.SFSObject();
                loginData.putUtfString('sessionToken', sessionToken);

                sfsInstance.send(new SFS2X.LoginRequest(userLoginId, "", loginData));
            });
        },


    });

    return {
        SmartFox: SfsExtended
    }
});