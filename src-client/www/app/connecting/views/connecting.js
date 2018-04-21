define(function (require) {

    var login = require('app/login');
    var connectingTemplate = require('hbs!app/connecting/templates/connecting');
    var remote = require('app/remote');

    var ConnectingView = Backbone.View.extend({
        className: "d-flex justify-content-center align-middle align-items-center flex-column h-50 w-100",
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
                    remote.invokeControllerMethod('home','displayHomeView');
                })
                .catch(function (appError) {
                    remote.invokeControllerMethod('error','displayErrorView', appError);
                });
        },
        _displayConnectingMessage: function () {
            var content = connectingTemplate();
            this.$el.html(content);
        },


    });



    return ConnectingView;

});