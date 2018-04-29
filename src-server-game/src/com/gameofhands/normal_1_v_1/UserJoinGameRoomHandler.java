package com.gameofhands.normal_1_v_1;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class UserJoinGameRoomHandler extends BaseServerEventHandler {

	@Override
	public void handleServerEvent(ISFSEvent event) {
		RoomExtension gameExtension = (RoomExtension) getParentExtension();
		gameExtension.gameStateMachine.onPlayerJoin();

	}

}
