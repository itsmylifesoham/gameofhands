define(function (require, exports, module) {
    var loginTemplate = require('hbs!app/login/templates/login');
    var facebook = require('app/facebook');
    var website = require('app/website');
    var globals = require('app/globals');

    var LoginView = Backbone.View.extend({
        className: 'd-flex justify-content-center align-middle align-items-center flex-column h-100 w-100',
        events: {
            'click #fb-login': 'onConnectWithFb',
        },
        onConnectWithFb: function () {
            var view = this;
            view.hideConnectButton();
            view.assignloginFlow(facebook.login());
        },
        hideConnectButton: function () {
            this.$("#fb-login").addClass("d-none");
        },
        showConnectButton: function () {
            this.$("#fb-login").removeClass("d-none");
        },
        addLog: function (newLog) {
            this.$("#log").html(this.$("#log").html() + "<br>" + newLog);
        },
        render: function () {
            var content = loginTemplate();
            this.$el.html(content);
            var view = this;
            view.hideConnectButton();
            view.assignloginFlow(facebook.loginStatus());
            return this;
        },
        assignloginFlow: function (loginPromise) {
            var view = this;
            loginPromise
                .then(function (userData) {
                    return website.login(userData.userId, userData.accessToken);
                })
                .then(function (loginResultPayload) {
                    return globals.app.sfs.connect()
                        .then(function (msg) {
                            return globals.app.sfs.login(loginResultPayload.userLoginId, loginResultPayload.sessionToken);
                        })
                        .then(function(){
                            console.log('user logged in!');
                        })
                        .catch(function(error){
                            return Promise.reject(error);
                        });
                })
                .catch(function (error) {
                    view.showConnectButton();
                });
        },
    });


    return LoginView;
});