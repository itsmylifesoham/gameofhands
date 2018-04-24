package com.gameofhands.normal_1_v_1;

import java.util.List;

import com.gameofhands.ExtensionReponses;
import com.gameofhands.SfsObjectKeys;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import com.smartfoxserver.v2.game.SFSGame;


public class UserJoinGameRoomHandler extends BaseServerEventHandler {

	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {
		SFSGame gameRoom = (SFSGame)getParentExtension().getParentRoom();
		List<User> usersInRoom = gameRoom.getUserList();
		if (gameRoom.getMinPlayersToStartGame() == usersInRoom.size()) {
			
			ISFSObject matchData1 = new SFSObject();
			matchData1.putUtfString(SfsObjectKeys.USER_LOGIN_ID, usersInRoom.get(0).getName());
			
			send(ExtensionReponses.DISPLAY_MATCH, matchData1, usersInRoom.get(1));
			
			ISFSObject matchData2 = new SFSObject();
			matchData2.putUtfString(SfsObjectKeys.USER_LOGIN_ID, usersInRoom.get(1).getName());
			
			send(ExtensionReponses.DISPLAY_MATCH, matchData1, usersInRoom.get(0));
		}	
	}
	
}
