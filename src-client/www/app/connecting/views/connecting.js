define(function (require) {

    var login = require('app/login');
    var connectingTemplate = require('hbs!app/connecting/templates/connecting');
    var homeController = require('app/home/controller');
    var errorController = require('app/error/controller');

    var ConnectingView = Backbone.View.extend({
        className: "d-flex justify-content-center align-middle align-items-center flex-column h-100 w-100",
        events: {
            'click #retry-login': 'render'
        },
        render: function () {
            this._displayConnectingMessage();
            this.startLogin();
            return this;
        },
        startLogin: function () {
            login.connectToFbAutomatically()
                .then(function (loginEvtParams) {
                    console.log("user logged in!");
                    homeController.displayHomeView();
                })
                .catch(function (appError) {
                    errorController.displayErrorView(appError);
                });
        },
        _displayConnectingMessage: function () {
            var content = connectingTemplate();
            this.$el.html(content);
        },


    });



    return ConnectingView;

});