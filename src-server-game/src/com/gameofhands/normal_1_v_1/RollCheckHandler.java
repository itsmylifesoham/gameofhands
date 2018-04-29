package com.gameofhands.normal_1_v_1;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class RollCheckHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User sender, ISFSObject params) {
		RoomExtension gameExtension = (RoomExtension) getParentExtension();
		gameExtension.rollCheckSuccessUsers.add(sender);

		if (gameExtension.rollCheckSuccessUsers.size() == gameExtension.getParentRoom().getMaxUsers()) {
			trace("found rollCheckSize same as 2!");
			gameExtension.setGameState(GameState.ROLL_CHECK_SUCCESS);
		}
	}

}
