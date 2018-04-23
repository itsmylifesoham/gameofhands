define(function(require){
    var globals = require('app/globals');
    var helpers = require('app/helpers');

    var appRequests = {
        JOIN_ME: 'JOIN_ME',
    };

    var appResponses = {
        UNABLE_TO_JOIN_GAME: 'UNABLE_TO_JOIN_GAME',
    };

    var appSfsObjectKeys = {
        GAME_FORMAT: 'GAME_FORMAT',
    };

    var gameFormats = {
        NORMAL_2_OVERS_1_WICKET: 'NORMAL_2_OVERS_1_WICKET',
        NORMAL_3_OVERS_2_WICKETS: 'NORMAL_3_OVERS_2_WICKETS',
        NORMAL_5_OVERS_2_WICKETS: 'NORMAL_5_OVERS_2_WICKETS',
        NORMAL_7_OVERS_3_WICKETS: 'NORMAL_7_OVERS_3_WICKETS',
        NORMAL_10_OVERS_3_WICKETS: 'NORMAL_10_OVERS_3_WICKETS',
    }


    var quickJoinGame = function(gameFormat){
        return new Promise(function(resolve, reject){
            function onRoomVariableUpdate(evtParams){
                var changedVars = evtParams.changedVars;
                var room = evtParams.room;

                // Check if the "gameStarted" variable was changed
                if (changedVars.indexOf(SFS2X.ReservedRoomVariables.RV_GAME_STARTED) != -1)
                {
                    if (room.getVariable(SFS2X.ReservedRoomVariables.RV_GAME_STARTED).value)
                        resolve();
                    else
                        reject();
                }
            }

            globals.app.sfs.addEventListener(SFS2X.SFSEvent.ROOM_VARIABLES_UPDATE, onRoomVariableUpdate, globals.app.sfs);

            var params = new SFS2X.SFSObject();
            params.putUtfString(appSfsObjectKeys.GAME_FORMAT, gameFormat);

            globals.app.sfs.send(new SFS2X.ExtensionRequest(appRequests.JOIN_ME, params));

        });
    }


    return {
        appRequests: appRequests,
        appResponses: appResponses,
        appSfsObjectKeys, appSfsObjectKeys,
        gameFormats: gameFormats,
        quickJoinGame: quickJoinGame,
    }
});