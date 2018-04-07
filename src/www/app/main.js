define(function (require, exports, module) {

    var router = require('app/router');

    var AppView = Backbone.View.extend({
        constructor: function (parent) {
            if (!parent)
                throw new Error("Please provide a parent element for the app");

            Backbone.View.call(this, {
                el: parent
            });
        },
        initialize: function(){
            this.router = router
        }
    });

   

    AppView.prototype.start = function () {
        Backbone.history.start();
        // delegate to the homeView controller here to append to the appView
    };

    return AppView;
});
