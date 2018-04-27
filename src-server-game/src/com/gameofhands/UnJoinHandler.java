package com.gameofhands;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class UnJoinHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User sender, ISFSObject params) {		
		getApi().leaveRoom(sender, sender.getLastJoinedRoom());		
	}

}
