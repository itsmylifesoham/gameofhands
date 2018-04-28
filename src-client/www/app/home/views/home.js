define(function(require){

    var homeTemplate = require('hbs!app/home/templates/home');
    var globals = require('app/globals');
    //var sfsCommunication = require('app/sfs-communication');
    var remote = require('app/remote');
    var Game_normal_1_v_1 = require('app/gaming/game-normal-1-v-1');
    var sfsObjectValues = require('app/gaming/sfs-object-values');

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
            this.startNewGame(new Game_normal_1_v_1(sfsObjectValues.NORMAL_1_V_1_OVERS_2_WICKETS_2));
            remote.invokeControllerMethod('game-normal-1-v-1/match', 'displayMatchView');
        },
        startNewGame: function(game){
            globals.app.game = game;
            globals.app.game.join();
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