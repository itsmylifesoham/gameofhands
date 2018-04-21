define(function (require) {

    var HomeView = require('app/home/views/home');
    var globals = require('app/globals');
    var AppController = require('app/common/app-controller');

    var _homeController = new AppController('home');

    _homeController.displayHomeView = function () {
        globals.app.setContent(new HomeView());
        globals.app.router.navigate('home');
    };

    return _homeController;
});