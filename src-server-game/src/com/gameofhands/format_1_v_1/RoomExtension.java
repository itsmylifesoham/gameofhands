package com.gameofhands.format_1_v_1;

import com.gameofhands.ExtensionRequests;
import com.gameofhands.format_1_v_1.handlers.GameBeginHandler;
import com.gameofhands.format_1_v_1.handlers.GameDisplayedHandler;
import com.gameofhands.format_1_v_1.handlers.GameLoadedHandler;
import com.gameofhands.format_1_v_1.handlers.RoleSelectHandler;
import com.gameofhands.format_1_v_1.handlers.RollCheckHandler;
import com.gameofhands.format_1_v_1.handlers.TossInputHandler;
import com.gameofhands.format_1_v_1.handlers.UserJoinGameRoomHandler;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class RoomExtension extends SFSExtension {

	public GameStateMachine_format_1_v_1 gameStateMachine;

	@Override
	public void init() {
		trace("Game Room created yay!!");

		gameStateMachine = new GameStateMachine_format_1_v_1(this);
		addEventHandler(SFSEventType.USER_JOIN_ROOM, UserJoinGameRoomHandler.class);
		addRequestHandler(ExtensionRequests.ROLL_CHECK, RollCheckHandler.class);
		addRequestHandler(ExtensionRequests.GAME_LOADED, GameLoadedHandler.class);
		addRequestHandler(ExtensionRequests.GAME_DISPLAYED, GameDisplayedHandler.class);
		addRequestHandler(ExtensionRequests.TOSS_INPUT, TossInputHandler.class);
		addRequestHandler(ExtensionRequests.ROLE_SELECT, RoleSelectHandler.class);
		addRequestHandler(ExtensionRequests.GAME_BEGIN, GameBeginHandler.class);

	}

	@Override
	public void destroy() {
		super.destroy();
		gameStateMachine.destroy();		
	}


}
