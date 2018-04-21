define(function (require) {

    var remote = require('app/remote');

    var _router = new Backbone.Router({
        routes: {
            '': function(){
                remote.invokeControllerMethod('connecting', 'displayConnectingView');
            },
            'home': function(){
                remote.invokeControllerMethod('home', 'displayHomeView');
            },
            'login': function(){
                remote.invokeControllerMethod('login', 'displayLoginView');
            },

        }
    });

    return _router;
});