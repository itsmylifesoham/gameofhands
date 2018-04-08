define(function (require, exports, module) {
    var homeTemplate = require('hbs!app/home/templates/home');
    var globals = require('app/globals');

    var HomeView = Backbone.View.extend({
        className: 'd-flex justify-content-center align-middle align-items-center flex-column h-100 w-100',
        events: {
            'click #fb-login': 'onConnectWithFb',
        },
        onConnectWithFb: function () {
            if (window.cordova.platformId == "browser") {
                facebookConnectPlugin.browserInit(globals.facebookAppId);
            }

            facebookConnectPlugin.login(["email"],
                function (response) {
                    alert(JSON.stringify(response))
                },
                function (response) {
                    alert(JSON.stringify(response))
                });

        },
        render: function () {
            var content = homeTemplate();
            this.$el.html(content);
            return this;
        }
    });


    return HomeView;
});