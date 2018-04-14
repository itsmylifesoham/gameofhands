define(function (require, exports, module) {

    var HomeView = require('app/home/views/home');
    var globals = require('app/globals');

    var _homeController = {};
    _homeController.displayHomeView = function () {
        globals.app.$el.html((new HomeView()).render().el);
        globals.app.router.navigate('');
    }

    return _homeController;
});