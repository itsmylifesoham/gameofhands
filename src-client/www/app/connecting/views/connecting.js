define(function (require) {

    var login = require('app/login');
    var homeController = require('app/home/controller');
    var loginController = require('app/login/controller');
    var errors = require('app/errors');
    var connectingInProcessTemplate = require('hbs!app/connecting/templates/connecting-in-process');
    var connectingErrorTemplate = require('hbs!app/connecting/templates/connecting-error');
    var internet = require("app/internet");

    var ConnectingView = Backbone.View.extend({
        className: "d-flex justify-content-center align-middle align-items-center flex-column h-100 w-100",
        events: {
            'click #retry-login': 'retryLogin'
        },
        retryLogin: function () {
            this._displayConnectingMessage();

            var view = this;
            internet.connected()
                .then(function () {
                    view.startLogin();
                })
                .catch(function (error) {
                    view.renderError(error);
                });
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
                || appError.errorType === errors.errorTypes.SFS_LOGIN_ERROR)
                this._displayErrorMessage(appError.displayMessage());
            else if (appError.errorType === errors.errorTypes.FACEBOOK_LOGIN_ERROR || appError.errorType === errors.errorTypes.WEBSITE_LOGIN_ERROR) {
                alert(appError.displayMessage());
                loginController.displayLoginView();
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