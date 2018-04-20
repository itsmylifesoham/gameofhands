define(function (require) {

    var login = require('app/login');
    var homeController = require('app/home/controller');
    var errors = require('app/errors');
    var connectingInProcessTemplate = require('hbs!app/connecting/templates/connecting-in-process');
    var connectingErrorTemplate = require('hbs!app/connecting/templates/connecting-error');
    var globals = require('app/globals');


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
            var view = this;
            login.connectToFbAutomatically()
                .then(function (loginEvtParams) {
                    console.log("user logged in!");
                    homeController.displayHomeView();
                })
                .catch(function (appError) {
                    view.renderError(appError);
                });
        },
        renderError: function (appError) {
            if (appError.errorType === errors.errorTypes.INTERNET_DISCONNECTED
                || appError.errorType === errors.errorTypes.SFS_CONNECTION_ERROR
                || appError.errorType === errors.errorTypes.SFS_LOGIN_ERROR
                || appError.errorType === errors.errorTypes.REQUEST_TIMEOUT_ERROR)
                this._displayErrorMessage(appError.displayMessage);
            else if (appError.errorType === errors.errorTypes.FACEBOOK_LOGIN_ERROR || appError.errorType === errors.errorTypes.WEBSITE_LOGIN_ERROR) {
                alert(appError.displayMessage);
                globals.app.router.navigate("login", true); // done this to break circular dependency between connecting and login controller. use madge to see
            }
            else {
                throw new Error("appError's type is not recognized!");
            }

            return this;
        },
        _displayConnectingMessage: function () {
            var content = connectingInProcessTemplate();
            this.$el.html(content);
        },
        _displayErrorMessage: function (errorMessage) {
            var content = connectingErrorTemplate({
                errorMessage: errorMessage,
            });
            this.$el.html(content);
        }

    });



    return ConnectingView;

});