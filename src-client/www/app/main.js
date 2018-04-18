define(function (require, exports, module) {

    var router = require('app/router');
    var sfs = require('app/sfs');
    var loginController = require('app/login/controller');
    var globals = require('app/globals');

    var AppView = Backbone.View.extend({
        constructor: function (rootElement) {
            if (!rootElement)
                throw new Error("Please provide a parent element for the app");

            Backbone.View.call(this, {
                el: rootElement
            });
        },
        initialize: function () {
            this.isOnline = true;
            this.router = router;
            this._initSFS();
            this._initNetworkPlugin();

        },
        _initNetworkPlugin: function(){
            var app = this;
            // add events for internet connectivity
            document.addEventListener("offline", function () {
                app._setDisconnected("Not connected to internet!");
            }, false);
            document.addEventListener("online", function () {
                app._setConnected();
            }, false);

        },
        _setDisconnected(reasonOffline){
            if (!this.isOnline)
                return;

            this.isOnline = false;
            alert(reasonOffline);
            loginController.displayLoginView();
        },
        _setConnected(){
            if (this.isOnline)
                return;

            this.isOnline = true;
            alert("app online!");
        },
        _initSFS: function () {
            this.sfs = new sfs.SmartFox(globals.sfsConfig);
            this._initSFSConnectionLostHandler();

        },
        _initSFSConnectionLostHandler: function(){
            var app = this;
            // init CONNECTION_LOST event
            this.sfs.addEventListener(SFS2X.SFSEvent.CONNECTION_LOST, function (evtParams) {
                var reason = evtParams.reason;
                var displayReason = "";
                if (reason != SFS2X.ClientDisconnectionReason.MANUAL) {
                    if (reason == SFS2X.ClientDisconnectionReason.IDLE)
                        displayReason = "A disconnection occurred due to inactivity.";
                    else if (reason == SFS2X.ClientDisconnectionReason.KICK)
                        displayReason = "You have been kicked by the moderator.";
                    else if (reason == SFS2X.ClientDisconnectionReason.BAN)
                        displayReason = "You have been banned by the moderator.";
                    else
                        displayReason = "A disconnection occurred. Please try again in sometime.";
                }
                else {
                    // Manual disconnection is usually ignored
                }

                // also check if connection was lost due to network error
                if (navigator.connection.type === Connection.NONE)
                    displayReason += "Not connected to internet!";

                app._setDisconnected(displayReason);

            }, app);
        },
        start: function () {
            
            Backbone.history.start();

        }
    });


    return AppView;
});
