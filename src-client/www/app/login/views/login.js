define(function (require, exports, module) {
    var loginTemplate = require('hbs!app/login/templates/login');
    var facebook = require('app/facebook');
    var website = require('app/website');
    var globals = require('app/globals');
    var homeController = require('app/home/controller');
    var login = require('app/login');

    var LoginView = Backbone.View.extend({
        className: 'd-flex justify-content-center align-middle align-items-center flex-column h-100 w-100',
        events: {
            'click #fb-login': 'onConnectWithFb',
            'click #guest-login': 'onPlayAsGuest'
        },
        onConnectWithFb: function () {
            var view = this;
            view.assignSuccessfulLoginFlow(login.connectToFbByDialog());
            view.hideConnectButtons();
        },
        onPlayAsGuest: function () {
            var view = this;
            view.assignSuccessfulLoginFlow(login.connectAsGuest());
            view.hideConnectButtons();
        },
        assignSuccessfulLoginFlow(sfsLoginPromise) {
            var view = this;
            sfsLoginPromise
                .then(function (loginEvtParams) {
                    console.log('user logged in!');
                    homeController.displayHomeView();
                })
                .catch(function () {
                    view.showConnectButtons();
                });
        },
        hideConnectButtons: function () {
            this.$("#connect-buttons").addClass("d-none");
        },
        showConnectButtons: function () {
            this.$("#connect-buttons").removeClass("d-none");
        },
        render: function () {
            var content = loginTemplate();
            this.$el.html(content);
            return this;
        },

    });


    return LoginView;
});