define(function (require) {

    var globals = require('app/globals');
    var extensionRequests = require('app/gaming/extension-requests');
    var sfsObjectKeys = require('app/gaming/sfs-object-keys');
    var extensionResponses = require('app/gaming/extension-responses');

    function handleGameExtensionResponse(evtParams) {
        if (evtParams.cmd === extensionResponses.DISPLAY_MATCH) {
            this.handleDisplayMatch(evtParams);
        }
    }


    var Game = function (gameFormat) {
        _.extend(this, Backbone.Events);
        this.gameFormat = gameFormat;
    };

    Game.prototype.init = function () {
        var gameInstance = this;
        globals.app.sfs.addEventListener(SFS2X.SFSEvent.EXTENSION_RESPONSE, handleGameExtensionResponse, gameInstance);
    }

    Game.prototype.join = function () {

        var params = new SFS2X.SFSObject();
        params.putUtfString(sfsObjectKeys.GAME_FORMAT, this.gameFormat);

        globals.app.sfs.send(new SFS2X.ExtensionRequest(extensionRequests.JOIN_ME, params));
    };

    Game.prototype.gameLoaded = function () {
        globals.app.sfs.send(new SFS2X.ExtensionRequest(extensionRequests.GAME_LOADED, null));
    };

    Game.prototype.tossSelect = function (num) {

        var params = new SFS2X.SFSObject();
        params.putInt(sfsObjectKeys.VALUE, num);

        globals.app.sfs.send(new SFS2X.ExtensionRequest(extensionRequests.TOSS_SELECT, params));
    };

    Game.prototype.input = function (num) {

        var params = new SFS2X.SFSObject();
        params.putInt(sfsObjectKeys.VALUE, num);

        globals.app.sfs.send(new SFS2X.ExtensionRequest(extensionRequests.INPUT, params));
    };

    Game.prototype.handleDisplayMatch = function (evtParams) {

        var opponentUserLoginId = evtParams.params.getUtfString(sfsObjectKeys.USER_LOGIN_ID);

        this.trigger(extensionResponses.DISPLAY_MATCH, {
            userLoginId: globals.app.sfs.mySelf.name
        }, {
            userLoginId: opponentUserLoginId
        })
    };


    Game.prototype.destroy = function () {
        globals.app.sfs.removeEventListener(SFS2X.EXTENSION_RESPONSE, handleGameExtensionResponse);
    };
    Game.prototype.constructor = Game;

    return Game;
});