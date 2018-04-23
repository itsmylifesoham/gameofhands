define(function(require){

    var homeTemplate = require('hbs!app/home/templates/home');
    var globals = require('app/globals');
    var sfsCommunication = require('app/sfs-communication');

    var HomeView = Backbone.View.extend({
        className: "d-flex justify-content-center align-middle align-items-center flex-column h-100 w-100",
        events: {
            'click #play-normal': 'onPlayNormal',
            'click #play-fixed-wickets': 'onPlayFixedWickets',
        },
        render: function(){
            var content = homeTemplate({
                userLoginId: globals.app.sfs.mySelf.name
            });
            this.$el.html(content);

            return this;
        },
        onPlayNormal: function(){
            sfsCommunication.quickJoinGame(sfsCommunication.gameFormats.NORMAL_2_OVERS_1_WICKET)
                .then(function(){
                    alert("game started!");
                })
                .catch(function(){
                    alert("game stopped!");
                });
        },
        onPlayFixedWickets: function(){
            sfsCommunication.quickJoinGame(sfsCommunication.gameFormats.NORMAL_3_OVERS_2_WICKETS)
                .then(function(){
                    alert("game started!");
                })
                .catch(function(){
                    alert("game stopped!");
                });
        }
    });

    return HomeView;
});