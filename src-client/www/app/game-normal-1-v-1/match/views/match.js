define(function (require) {

    // below will be synchronous calls as we have dependency array
    var matchTemplate = require('hbs!app/game-normal-1-v-1/match/templates/match');
    var globals = require('app/globals');
    var remote = require('app/remote');
    var extensionResponses = require('app/gaming/extension-responses');
    var errors = require('app/errors');

    var MatchView = Backbone.View.extend({
        className: 'd-flex justify-content-center align-middle align-items-center flex-column h-100 w-100',
        initialize: function () {
            var game = globals.app.game;
            var view = this;
            this.listenTo(game, extensionResponses.DISPLAY_MATCH, _.bind(view.handleDisplayMatch, view));
            this.listenTo(game, extensionResponses.UNABLE_TO_JOIN, _.bind(view.handleUnableToJoin, view));
        },
        events:{
            'click #leave': 'onLeave',
        },
        onLeave: function(){
            globals.app.game.destroy();
            remote.invokeControllerMethod('home','displayHomeView');
        },
        handleUnableToJoin: function(){
            globals.app.game.destroy();
            remote.invokeControllerMethod('error','displayErrorView',new errors.UnableToJoinError());
        },
        handleDisplayMatch: function (myData, opponentData) {
            this.presenter = {
                userLoginIdMyself: myData.userLoginId,
                userLoginIdOpponent: opponentData.userLoginId,
            };

            this.render();
        },
        render: function () {
            var content = matchTemplate(this.presenter);
            this.$el.html(content);
            return this;
        },

    });


    return MatchView;
});