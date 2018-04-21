define(function (require) {

    var ConnectingView = require('app/connecting/views/connecting');
    var globals = require('app/globals');
    var AppController = require('app/common/app-controller');

    var _connectingController = new AppController('connecting');
    _connectingController.displayConnectingView = function () {

        if (globals.app.currentView)
            globals.app.currentView.remove();

        globals.app.currentView = new ConnectingView();
        // prepending because there could be any errors thrown synchronusely like on mobile and we want to show them.
        globals.app.$el.prepend(globals.app.currentView.render().el);
        globals.app.router.navigate('');
    };

    return _connectingController;
});