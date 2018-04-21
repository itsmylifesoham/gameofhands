define(function (require, exports, module) {

    var LoginView = require('app/login/views/login');
    var globals = require('app/globals');
    var AppController = require('app/common/app-controller');

    var _loginController = new AppController('login');
    _loginController.displayLoginView = function () {

        if (globals.app.currentView)
            globals.app.currentView.remove();

        globals.app.currentView = new LoginView();
        globals.app.$el.prepend(globals.app.currentView.render().el);
        globals.app.router.navigate('login');
    };

    return _loginController;
});