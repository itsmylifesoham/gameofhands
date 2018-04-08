define(function(require, exports, module){

    var homeController = require('app/home/controller');

    var _router = new Backbone.Router({
        routes: {
            '': homeController.displayHomeView
        }
    });

    return _router;
});