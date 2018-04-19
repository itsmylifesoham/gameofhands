define(function (require, exports, module) {


    var homeController = require('app/home/controller');
    var connectingController = require('app/connecting/controller');

    var _router = new Backbone.Router({
        routes: {
            '': connectingController.displayConnectingView,
            'home': homeController.displayHomeView
        }
    });

    return _router;
});