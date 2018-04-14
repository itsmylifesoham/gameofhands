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
            facebook.login()
                .then(function (userData) {
                    return website.login(userData.userId, userData.accessToken);
                })
                .then(function (loginResultPayload) {
                    return globals.app.sfs.connect(globals.sfs.host, globals.sfs.port);
                })
                .then(function(msg){
                    view.addLog(msg);
                })
                .catch(function (error) {
                    view.addLog(JSON.stringify(error));
                    view.showConnectButton();
                });
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
            facebook.loginStatus()
                .then(function (userData) {
                    return website.login(userData.userId, userData.accessToken);
                })
                .then(function (loginResultPayload) {
                    return globals.app.sfs.connect(globals.sfs.host, globals.sfs.port);
                })
                .then(function(msg){
                    view.addLog(msg);
                })
                .catch(function (error) {
                    view.showConnectButton();
                });

            return this;
        }
    });


    return LoginView;
});