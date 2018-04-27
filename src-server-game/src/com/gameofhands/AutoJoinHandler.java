package com.gameofhands;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class AutoJoinHandler extends BaseClientRequestHandler {	
	
	@Override
	public void handleClientRequest(User sender, ISFSObject params) {
		String gameFormatSubCategory = params.getUtfString(SfsObjectKeys.GAME_FORMAT_SUBCATEGORY);
		try {			
			getApi().joinRoom(sender, getParentExtension().getParentZone().getRoomByName("JOIN_ME_" + gameFormatSubCategory));
		} catch (Exception e) {			
			send(ExtensionReponses.UNABLE_TO_JOIN, null, sender);
		}	
	}	
}
