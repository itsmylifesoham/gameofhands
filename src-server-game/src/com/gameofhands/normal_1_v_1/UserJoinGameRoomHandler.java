package com.gameofhands.normal_1_v_1;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import com.smartfoxserver.v2.game.SFSGame;


public class UserJoinGameRoomHandler extends BaseServerEventHandler {

	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {
		RoomExtension gameExtension = (RoomExtension)getParentExtension();
		SFSGame gameRoom = (SFSGame)gameExtension.getParentRoom();
		
		if (gameRoom.getMinPlayersToStartGame() == gameRoom.getPlayersList().size()) {			
			gameExtension.setGameState(GameState.FILLED);
			
		}	
	}

	
}
