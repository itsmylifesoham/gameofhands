package com.gameofhands.format_1_v_1.handlers;

import com.gameofhands.SfsObjectKeys;
import com.gameofhands.format_1_v_1.RoomExtension;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class RoleSelectHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User sender, ISFSObject params) {
		RoomExtension gameExtension = (RoomExtension) getParentExtension();
		gameExtension.gameStateMachine.onSelectRole(sender, params.getUtfString(SfsObjectKeys.PLAYER_ROLE));
	}

}
