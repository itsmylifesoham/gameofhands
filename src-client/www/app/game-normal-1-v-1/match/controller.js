define(function (require, exports, module) {

    var MatchView = require('app/game-normal-1-v-1/match/views/match');
    var globals = require('app/globals');
    var AppController = require('app/common/app-controller');

    var _matchController = new AppController('game-normal-1-v-1/match');
    _matchController.displayMatchView = function () {
        globals.app.setContent(new MatchView());
        globals.app.router.navigate('match');
    };

    return _matchController;
});