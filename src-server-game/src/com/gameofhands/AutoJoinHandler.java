package com.gameofhands;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.api.ISFSGameApi;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSJoinRoomException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class AutoJoinHandler extends BaseClientRequestHandler {
	@Override
	public void handleClientRequest(User sender, ISFSObject params) {
		String gameFormat = params.getUtfString(AppSfsObjectKeys.GAME_FORMAT.name());

		try {
			ISFSGameApi gameApi = SmartFoxServer.getInstance().getAPIManager().getGameApi();
			Zone zone = getParentExtension().getParentZone();			
			gameApi.quickJoinGame(sender, null, getParentExtension().getParentZone(), gameFormat);
			
		} catch (Exception e1) {
			createNewRoomForUser(sender, gameFormat);
		}
	
	}
	
	private void createNewRoomForUser(User user, String gameFormat) {
		try {
			GameFormatManager.getGameFormatManager(gameFormat).createGameRoomAndAddUser(user);

		} catch (Exception e2) {
			ISFSObject errorParams = new SFSObject();
			errorParams.putUtfString("errorMessage", "Unable to Join a game at this moment.");
			send(AppReponses.UNABLE_TO_JOIN_GAME.name(), errorParams, user);
		}
	}
}
