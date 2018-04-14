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
    });

    return {
        SmartFox: SfsExtended
    }
});