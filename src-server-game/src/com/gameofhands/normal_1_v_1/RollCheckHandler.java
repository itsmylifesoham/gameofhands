package com.gameofhands.normal_1_v_1;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class RollCheckHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User sender, ISFSObject params) {
		RoomExtension gameExtension = (RoomExtension) getParentExtension();
		gameExtension.gameStateMachine.onRollCheck(sender);;
	}

}
