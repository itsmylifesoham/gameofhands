package com.gameofhands.normal_1_v_1;

import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class RoomExtension extends SFSExtension {

	@Override
	public void init() {
		trace("Game Room created yay!!");
		
		addEventHandler(SFSEventType.USER_JOIN_ROOM, UserJoinGameRoomHandler.class);
	}

}
