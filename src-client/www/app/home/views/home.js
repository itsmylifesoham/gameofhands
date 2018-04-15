define(function(require){

    var homeTemplate = require('hbs!app/home/templates/home');
    var globals = require('app/globals');

    var HomeView = Backbone.View.extend({
        className: "d-flex justify-content-center align-middle align-items-center flex-column h-100 w-100",
        render: function(){
            var content = homeTemplate({
                userLoginId: globals.app.sfs.mySelf.name
            });
            this.$el.html(content);

            return this;
        }
    });

    return HomeView;
});