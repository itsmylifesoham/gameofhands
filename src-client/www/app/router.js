define(function(require, exports, module){

    var loginController = require('app/login/controller');
    var homeController = require('app/home/controller');

    var _router = new Backbone.Router({
        routes: {
            '': loginController.displayLoginView,
            'home': homeController.displayHomeView        }
    });

    return _router;
});