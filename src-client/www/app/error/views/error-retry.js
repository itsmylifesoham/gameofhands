define(function(require){

    var errorRetryTemplate = require('hbs!app/error/templates/error-retry');
    var globals =require('app/globals');
    var remote = require('app/remote');

    var ErrorRetryView = Backbone.View.extend({
        initialize: function(appError){
            this.appError = appError;
        },
        className: "d-flex justify-content-center align-middle align-items-center flex-column h-100 w-100",

        events: {
            'click #retry-login' : 'retryLogin'
        },
        render: function(){

            var content = errorRetryTemplate({
                errorMsg: this.appError.displayMessage
            });

            this.$el.html(content);
            return this;
        },
        retryLogin: function(){
            globals.app.error = false;
            remote.invokeControllerMethod('connecting', 'displayConnectingView');
        }

    });

    return ErrorRetryView;
});