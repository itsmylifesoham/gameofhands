define(function (require) {

    var connectingController = require('app/connecting/controller');
    var loginController = require('app/login/controller');
    var homeController = require('app/home/controller');

    var _router = new Backbone.Router({
        routes: {
            '': connectingController.displayConnectingView,
            'home': homeController.displayHomeView,
            'login': loginController.displayLoginView,

        }
    });

    return _router;
});