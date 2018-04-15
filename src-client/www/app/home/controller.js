define(function (require) {

    var HomeView = require('app/home/views/home');
    var globals = require('app/globals');

    var _homeController = {};
    _homeController.displayHomeView = function () {

        if (globals.app.currentView)
            globals.app.currentView.remove();

        globals.app.currentView = new HomeView();
        globals.app.$el.html(globals.app.currentView.render().el);
        globals.app.router.navigate('home');
    }

    return _homeController;
});