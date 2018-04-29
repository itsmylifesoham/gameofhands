package com.gameofhands.normal_1_v_1;

import com.gameofhands.ExtensionRequests;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class RoomExtension extends SFSExtension {

	public GameStateMachine_normal_1_v_1 gameStateMachine;

	@Override
	public void init() {
		trace("Game Room created yay!!");

		gameStateMachine = new GameStateMachine_normal_1_v_1(this);
		addEventHandler(SFSEventType.USER_JOIN_ROOM, UserJoinGameRoomHandler.class);
		addRequestHandler(ExtensionRequests.ROLL_CHECK, RollCheckHandler.class);

	}

	@Override
	public void destroy() {
		super.destroy();
		gameStateMachine.destroy();		
	}


}
