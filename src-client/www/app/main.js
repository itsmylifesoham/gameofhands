define(function (require, exports, module) {

    var router = require('app/router');
    var sfs = require('app/sfs');
    var connectingController = require('app/connecting/controller');
    var globals = require('app/globals');
    var internet = require('app/internet');
    var errors = require('app/errors');

    var AppView = Backbone.View.extend({
        constructor: function (rootElement) {
            if (!rootElement)
                throw new Error("Please provide a parent element for the app");

            Backbone.View.call(this, {
                el: rootElement
            });
        },
        initialize: function () {
            this._isOnline = true;
            this.router = router;
            this._initSFS();
            this._initNetworkMonitoring();
        },
        isConnected: function () {
            return this._isOnline;
        },
        _initNetworkMonitoring: function () {
            var app = this;
            this.listenTo(internet, "online", function () {
                app.setConnected();
            });
            this.listenTo(internet, "offline", function () {
                app.setDisconnected("Not connected to internet!");
            });
        },
        setDisconnected(reasonOffline) {
            if (!this._isOnline)
                return;

            this._isOnline = false;
            alert(reasonOffline ? reasonOffline: "went offline");
            connectingController.displayConnectingViewWithError(new errors.InternetDisconnectedError(reasonOffline));
        },
        setConnected() {
            if (this._isOnline)
                return;

            this._isOnline = true;
            alert("app online!");
        },
        _initSFS: function () {
            this.sfs = new sfs.SmartFox(globals.sfsConfig);
            this._initSFSConnectionLostHandler();

        },
        _initSFSConnectionLostHandler: function () {
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

                app.setDisconnected(displayReason);

            }, app);
        },
        start: function () {
            Backbone.history.start();
        }
    });


    return AppView;
});
