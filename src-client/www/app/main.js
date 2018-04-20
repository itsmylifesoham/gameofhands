define(function (require, exports, module) {

    var router = require('app/router');
    var sfs = require('app/sfs');
    var globals = require('app/globals');
    var internet = require('app/internet');
    var errors = require('app/errors');
    var errorController = require('app/error/controller');

    var AppView = Backbone.View.extend({
        constructor: function (rootElement) {
            if (!rootElement)
                throw new Error("Please provide a parent element for the app");

            Backbone.View.call(this, {
                el: rootElement
            });
        },
        initialize: function () {
            this.router = router;
            this.error = false;
            this._initSFS();
            this._initNetworkMonitoring();
        },
        _initNetworkMonitoring: function () {
            this.listenTo(internet, "online", function () {

            });
            this.listenTo(internet, "offline", function () {
                errorController.displayErrorView(new errors.InternetDisconnectedError());
            });
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
                    displayReason = "You disconnected from the game server."
                }

                errorController.displayErrorView(new errors.SFSConnectionError(displayReason));

            }, app);
        },
        start: function () {
            Backbone.history.start();
        }
    });


    return AppView;
});
