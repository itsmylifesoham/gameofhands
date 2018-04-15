define(function (require, exports, module) {
    var loginTemplate = require('hbs!app/login/templates/login');
    var facebook = require('app/facebook');
    var website = require('app/website');
    var globals = require('app/globals');

    var LoginView = Backbone.View.extend({
        className: 'd-flex justify-content-center align-middle align-items-center flex-column h-100 w-100',
        events: {
            'click #fb-login': 'onConnectWithFb',
            'click #guest-login': 'onPlayAsGuest'
        },
        onConnectWithFb: function () {
            var view = this;
            view.hideConnectButtons();
            view.assignloginFbFlow(facebook.login());
        },
        onPlayAsGuest: function () {
            var view = this;
            view.hideConnectButtons();
            view.assignSfsLoginFlow(website.loginGuest());
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
            var view = this;
            view.hideConnectButtons();
            view.assignloginFbFlow(facebook.loginStatus());
            return this;
        },
        assignloginFbFlow: function (fbLoginPromise) {
            var view = this;
            var websiteLoginPromise = fbLoginPromise
                .then(function (userData) {
                    return website.loginFb(userData.userId, userData.accessToken);
                });
            view.assignSfsLoginFlow(websiteLoginPromise);

        },
        assignSfsLoginFlow: function (websiteLoginPromise) {
            var view = this;
            websiteLoginPromise
                .then(function (loginResultPayload) {
                    return globals.app.sfs.connect()
                        .then(function (msg) {
                            return globals.app.sfs.login(loginResultPayload.userLoginId, loginResultPayload.sessionToken);
                        })
                        .then(function () {
                            console.log('user logged in!');
                        })
                        .catch(function (error) {
                            return Promise.reject(error);
                        });
                })
                .catch(function (error) {
                    view.showConnectButtons();
                });
        },

    });


    return LoginView;
});