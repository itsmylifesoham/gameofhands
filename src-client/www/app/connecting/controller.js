define(function (require) {

    var ConnectingView = require('app/connecting/views/connecting');
    var globals = require('app/globals');

    var _connectingController = {};
    _connectingController.displayConnectingView = function () {

        if (globals.app.currentView)
            globals.app.currentView.remove();

        globals.app.currentView = new ConnectingView();
        globals.app.$el.html(globals.app.currentView.render().el);
        globals.app.router.navigate('');
    };
    _connectingController.displayConnectingViewWithError = function (appError) {

        if (globals.app.currentView){
            if(!(globals.app.currentView instanceof ConnectingView)){
                globals.app.currentView.remove();
            }
            else {
                // if the current view is a connecting view then ignore any new errors as we may be waiting for user action
                // or trying to connect already
                return;
            }
        }

        globals.app.currentView = new ConnectingView();
        globals.app.$el.html(globals.app.currentView.renderError(appError).el);
        globals.app.router.navigate('');
    }


    return _connectingController;
});