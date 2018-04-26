package com.gameofhands;

import java.util.Set;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.sun.javafx.collections.MappingChange.Map;

public class AutoJoinHandler extends BaseClientRequestHandler {	
	
	@Override
	public void handleClientRequest(User sender, ISFSObject params) {
		String gameFormat = params.getUtfString(SfsObjectKeys.GAME_FORMAT);
		try {			
			getApi().joinRoom(sender, getParentExtension().getParentZone().getRoomByName("JOIN_ME_" + gameFormat));
		} catch (Exception e) {			
			send(ExtensionReponses.UNABLE_TO_JOIN, null, sender);
		}	
	}	
}
