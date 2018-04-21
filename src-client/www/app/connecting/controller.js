define(function (require) {

    var ConnectingView = require('app/connecting/views/connecting');
    var globals = require('app/globals');
    var AppController = require('app/common/app-controller');

    var _connectingController = new AppController('connecting');
    _connectingController.displayConnectingView = function () {
        globals.app.setContent(new ConnectingView());
        globals.app.router.navigate('');
    };

    return _connectingController;
});