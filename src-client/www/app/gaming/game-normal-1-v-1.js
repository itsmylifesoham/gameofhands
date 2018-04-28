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

    function handleJoinMeRoomJoinError(evtParams) {
        //TODO: show a message here which is not an error screen
        alert("could not join room :" + evtParams.errorMessage);
        globals.app.sfs.removeEventListener(SFS2X.SFSEvent.ROOM_JOIN_ERROR, handleJoinMeRoomJoinError);
    }


    var Game = function (gameFormatSubCategory) {
        _.extend(this, Backbone.Events);
        this.gameFormatSubCategory = gameFormatSubCategory;
    };

    Game.prototype.init = function () {
        var gameInstance = this;
        globals.app.sfs.addEventListener(SFS2X.SFSEvent.EXTENSION_RESPONSE, handleGameExtensionResponse, gameInstance);
    }

    Game.prototype.join = function () {
        globals.app.sfs.addEventListener(SFS2X.SFSEvent.ROOM_JOIN_ERROR, handleJoinMeRoomJoinError);
        globals.app.sfs.send(new SFS2X.JoinRoomRequest('JOIN_ME_' + this.gameFormatSubCategory));
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
        if (globals.app.sfs.isConnected && globals.app.sfs.mySelf) {
            // leave the user from all jon me or game rooms
            var roomsJoined  = globals.app.sfs.getJoinedRooms();
            for(var room in roomsJoined){
                if (room.groupId === this.gameFormatSubCategory || room.groupId === "JOIN_ME_" + this.gameFormatSubCategory)
                    globals.app.sfs.send(new SFS2X.LeaveRoomRequest(joinRoom));
            }
        }

        globals.app.sfs.removeEventListener(SFS2X.EXTENSION_RESPONSE, handleGameExtensionResponse);
        delete globals.app.game;
    };
    Game.prototype.constructor = Game;

    return Game;
});