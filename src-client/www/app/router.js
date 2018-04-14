define(function(require, exports, module){

    var loginController = require('app/login/controller');

    var _router = new Backbone.Router({
        routes: {
            '': loginController.displayLoginView
        }
    });

    return _router;
});