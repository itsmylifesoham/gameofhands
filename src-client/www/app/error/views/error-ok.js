define(function(require){

    var errorOkTemplate = require('hbs!app/error/templates/error-ok');
    var globals =require('app/globals');

    var ErrorOkView = Backbone.View.extend({
        initialize: function(appError, okAction){
            this.appError = appError;
            this.okAction = okAction;
        },
        className: "d-flex justify-content-center align-middle align-items-center flex-column h-100 w-100",
        events: {
            'click #ok' : 'ok'
        },
        render: function(){

            var content = errorOkTemplate({
                errorMsg: this.appError.displayMessage
            });

            this.$el.html(content);
            return this;
        },
        ok: function(){
            globals.app.error = false;
            this.okAction();
            this.remove();
        }

    });

    return ErrorOkView;
});