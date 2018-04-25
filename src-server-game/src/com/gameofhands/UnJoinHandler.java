package com.gameofhands;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class UnJoinHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User sender, ISFSObject params) {
		
		
		if (sender.containsProperty(VariableKeys.JOIN_ME)) {
			sender.setProperty(VariableKeys.JOIN_ME, false);
		}

		if (sender.isPlayer() && sender.getLastJoinedRoom().isGame()) {
			getApi().leaveRoom(sender, sender.getLastJoinedRoom());
		}

	}

}
