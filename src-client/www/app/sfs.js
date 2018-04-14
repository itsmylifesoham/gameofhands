define(function(require){

    var helpers = require('app/helpers');
    SFS2X.SmartFox.extend = helpers.extend;
    var SfsExtended = SFS2X.SmartFox.extend({
        connect: function (ip, port){
            port = parseInt(port);
            var sfsInstance = this;

            return new Promise(function(resolve, reject){
                sfsInstance.addEventListener(SFS2X.SFSEvent.CONNECTION, onConnection, sfsInstance);

                function onConnection (evtParams){
                    if (evtParams.success)
                        resolve("connected to sfs!");
                    else
                        reject("unable to connect to sfs");
                };

                SFS2X.SmartFox.prototype.connect.call(sfsInstance, ip, port);
            });
        },
        connectionLost: function(){
            var sfsInstance = this;

            return new Promise(function(resolve, reject){
                sfsInstance.addEventListener(SFS2X.SFSEvent.CONNECTION_LOST, onConnectionLost, sfsInstance);

                function onConnectionLost(evtParams)
                {
                    var reason = evtParams.reason;

                    if (reason != SFS2X.ClientDisconnectionReason.MANUAL)
                    {
                        if (reason == SFS2X.ClientDisconnectionReason.IDLE)
                            resolve("A disconnection occurred due to inactivity");
                        else if (reason == SFS2X.ClientDisconnectionReason.KICK)
                            resolve("You have been kicked by the moderator");
                        else if (reason == SFS2X.ClientDisconnectionReason.BAN)
                            resolve("You have been banned by the moderator");
                        else
                            resolve("A disconnection occurred. Please try again.");
                    }
                    else
                    {
                        // Manual disconnection is usually ignored
                    }
                }
            });
        },
        login: function(userLoginId, sfsToken){

        }
    });

    return {
        SmartFox: SfsExtended
    }
});