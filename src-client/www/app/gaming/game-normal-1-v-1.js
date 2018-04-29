define(function (require) {

    var globals = require('app/globals');
    var extensionRequests = require('app/gaming/extension-requests');
    var extensionResponses = require('app/gaming/extension-responses');
    var sfsObjectKeys = require('app/gaming/sfs-object-keys');

    function handleGameExtensionResponse(evtParams) {
        switch (evtParams.cmd) {
            case extensionResponses.DISPLAY_MATCH:
                handleDisplayMatch.call(this, evtParams);
                break;
            default:
                this.trigger(evtParams.cmd, evtParams);

        }
    }


    function handleDisplayMatch(evtParams) {
        var opponentUserLoginId = evtParams.params.getUtfString(sfsObjectKeys.USER_LOGIN_ID);

        var gameRoom = globals.app.sfs.roomManager.getRoomListFromGroup(this.gameFormatSubCategory)[0];
        try{
            globals.app.sfs.send(new SFS2X.ExtensionRequest(extensionRequests.ROLL_CHECK, null, gameRoom));
        }
        catch (e) {
            // do nothing coz the net cord might be pulled out at this point and server wont receive this message anyway
        }

        this.trigger(extensionResponses.DISPLAY_MATCH, {
            userLoginId: globals.app.sfs.mySelf.name
        }, {
            userLoginId: opponentUserLoginId
        })
    }


    function handleJoinMeRoomJoinError(evtParams) {
        this.trigger(extensionResponses.UnableToJoinError);
    }


    var Game = function (gameFormatSubCategory) {
        _.extend(this, Backbone.Events);
        this.gameFormatSubCategory = gameFormatSubCategory;

        var gameInstance = this;
        // init eventhandlers
        globals.app.sfs.addEventListener(SFS2X.SFSEvent.ROOM_JOIN_ERROR, handleJoinMeRoomJoinError, gameInstance);
        globals.app.sfs.addEventListener(SFS2X.SFSEvent.EXTENSION_RESPONSE, handleGameExtensionResponse, gameInstance);
    };


    Game.prototype.join = function () {
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


    Game.prototype.destroy = function () {
        if (globals.app.sfs.isConnected && globals.app.sfs.mySelf) {
            // leave the user from all jon me or game rooms
            var roomsJoined = globals.app.sfs.getJoinedRooms();
            for (var i in roomsJoined) {
                if (roomsJoined[i].groupId === this.gameFormatSubCategory || roomsJoined[i].name === "JOIN_ME_" + this.gameFormatSubCategory)
                    globals.app.sfs.send(new SFS2X.LeaveRoomRequest(roomsJoined[i]));
            }
        }

        // remove all event handlers assigned for gameplay or game joining
        globals.app.sfs.removeEventListener(SFS2X.SFSEvent.ROOM_JOIN_ERROR, handleJoinMeRoomJoinError);
        globals.app.sfs.removeEventListener(SFS2X.SFSEvent.EXTENSION_RESPONSE, handleGameExtensionResponse);
        delete globals.app.game;
    };
    Game.prototype.constructor = Game;

    return Game;
});