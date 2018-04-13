define(function (require, exports, module) {

    var router = require('app/router');
    var homeController = require('app/home/controller');

    var AppView = Backbone.View.extend({
        constructor: function (rootElement) {
            if (!rootElement)
                throw new Error("Please provide a parent element for the app");

            Backbone.View.call(this, {
                el: rootElement
            });
        },
        initialize: function(){
            this.router = router
        }
    });

   

    AppView.prototype.start = function () {
        Backbone.history.start();
        homeController.displayHomeView();
    };

    return AppView;
});
