define(function (require, exports, module) {
    var homeTemplate = require('hbs!app/home/templates/home');
    var facebook = require('app/facebook');

    var HomeView = Backbone.View.extend({
        className: 'd-flex justify-content-center align-middle align-items-center flex-column h-100 w-100',
        events: {
            'click #fb-login': 'onConnectWithFb',
        },
        onConnectWithFb: function () {
            var homeView = this;

            facebook.login(function loginSuccess(userData) {
                homeView.$("#loadingMessage").removeClass("d-none");
                homeView.$("#fb-login").addClass("d-none");
                homeView.$("#loadingMessage").text("user is logged in as " + JSON.stringify(userData));
            }, function loginError() {
                homeView.$("#loadingMessage").removeClass("d-none");
                homeView.$("#fb-login").removeClass("d-none");
                homeView.$("#loadingMessage").text("There was a problem with login dialog. Please try again");

            });

        },
        render: function () {
            var content = homeTemplate();
            this.$el.html(content);

            var homeView = this;
            _.defer(function () {
                facebook.getLoggedInUser(function alreadyLoggedIn(userData) {
                    homeView.$("#loadingMessage").removeClass("d-none");
                    homeView.$("#fb-login").addClass("d-none");
                    homeView.$("#loadingMessage").text("user is logged in as " + JSON.stringify(userData));
                }, function notLoggedIn() {
                    homeView.$("#loadingMessage").addClass("d-none");
                    homeView.$("#fb-login").removeClass("d-none");
                })
            });

            return this;
        }
    });


    return HomeView;
});