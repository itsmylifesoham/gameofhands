define(function (require, exports, module) {

    var LoginView = require('app/login/views/login');
    var globals = require('app/globals');
    var AppController = require('app/common/app-controller');

    var _loginController = new AppController('login');
    _loginController.displayLoginView = function () {
        globals.app.setContent(new LoginView());
        globals.app.router.navigate('login');
    };

    return _loginController;
});