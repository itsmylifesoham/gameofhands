define(function (require) {

    var ConnectingView = require('app/connecting/views/connecting');
    var globals = require('app/globals');

    var _connectingController = {};
    _connectingController.displayConnectingView = function () {

        if (globals.app.currentView)
            globals.app.currentView.remove();

        globals.app.currentView = new ConnectingView();
        globals.app.$el.html(globals.app.currentView.render().el);
        globals.app.router.navigate('connecting');
    };
    _connectingController.displayConnectingViewWithError = function (appError) {

        if (globals.app.currentView)
            globals.app.currentView.remove();

        globals.app.currentView = new ConnectingView();
        globals.app.$el.html(globals.app.currentView.renderError(appError).el);
        globals.app.router.navigate('connecting');
    }


    return _connectingController;
});