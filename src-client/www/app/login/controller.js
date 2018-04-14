define(function (require, exports, module) {

    var LoginView = require('app/login/views/login');
    var globals = require('app/globals');

    var _loginController = {};
    _loginController.displayLoginView = function () {
        globals.app.$el.html((new LoginView()).render().el);
        globals.app.router.navigate('');
    }

    return _loginController;
});