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
        initialize: function(){
            this.isOnline = true;
            this.router = router;
            this.sfs = new sfs.SmartFox(globals.sfsConfig);
            this.sfs.connectionLost().then(function(reason){
                alert(reason);
                loginController.displayLoginView();
            });

            var app = this;
            this.listenTo(Backbone, "apponline", _.bind(app.onOnline, app));
            this.listenTo(Backbone, "appoffline", _.bind(app.onOffline, app));
        },

        onOnline: function(){
            this.isOnline = true;
            alert("app online!");
        },
        onOffline: function(){
            // handling multiple offline events
            if (!this.isOnline)
                return;

            this.isOnline = false;
            alert("app offline!");
        },
        start: function () {
            Backbone.history.start();
        }
    });


    return AppView;
});
