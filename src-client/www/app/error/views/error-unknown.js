define(function(require){

    var errorUnknownTemplate = require('hbs!app/error/templates/error-unknown');

    var ErrorUnknownView = Backbone.View.extend({
        className: "d-flex justify-content-center align-middle align-items-center flex-column h-100 w-100",

        render: function(){

            var content = errorUnknownTemplate();
            this.$el.html(content);
            return this;
        },

    });

    return ErrorUnknownView;
});