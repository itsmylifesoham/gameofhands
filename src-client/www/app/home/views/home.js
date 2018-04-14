define(function (require, exports, module) {
    var homeTemplate = require('hbs!app/home/templates/home');
    var facebook = require('app/facebook');
    var globals = require('app/globals');
    var HomeView = Backbone.View.extend({
        className: 'd-flex justify-content-center align-middle align-items-center flex-column h-100 w-100',
        events: {
            'click #fb-login': 'onConnectWithFb',
        },
        onConnectWithFb: function () {
            var homeView = this;

            facebook.login().then(function(userData){
                homeView.hideConnectButton();
                homeView.addLog('logged in as: ' + userData.userId);
                homeView.addLog('can connect to website now.');
            }).catch(function(error){
                homeView.addLog(error);
                homeView.showConnectButton();
            });
        },
        hideConnectButton: function(){
            this.$("#fb-login").addClass("d-none");
        },
        showConnectButton: function(){
            this.$("#fb-login").removeClass("d-none");
        },
        addLog: function(newLog){
            this.$("#log").html(this.$("#log").html() + "<br>" + newLog);
        },
        render: function () {
            var content = homeTemplate();
            this.$el.html(content);
            var homeView = this;

            facebook.loginStatus().then(function(userData){
                homeView.hideConnectButton();
                homeView.addLog('logged in as: ' + userData.userId);
                homeView.addLog('can connect to website now.');
            }).catch(function(error){
                homeView.addLog(error);
                homeView.showConnectButton();
            });

            return this;
        }
    });


    return HomeView;
});