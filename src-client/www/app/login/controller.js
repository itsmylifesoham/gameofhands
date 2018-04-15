define(function (require, exports, module) {

    var LoginView = require('app/login/views/login');
    var globals = require('app/globals');

    var _loginController = {};
    _loginController.displayLoginView = function () {
        globals.app.currentView = new LoginView();
        globals.app.$el.html(globals.app.currentView.render().el);
        globals.app.router.navigate('');
    }

    return _loginController;
});