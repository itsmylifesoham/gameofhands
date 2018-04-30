package com.gameofhands.format_1_v_1.timeouts;

import com.gameofhands.Constants;
import com.gameofhands.GameTimeout;
import com.gameofhands.format_1_v_1.GameStateMachine_format_1_v_1;
import com.gameofhands.format_1_v_1.GameState_format_1_v_1;

public class DisplayGameTimeout extends GameTimeout<GameStateMachine_format_1_v_1, GameState_format_1_v_1> {
	
	
	public DisplayGameTimeout(GameStateMachine_format_1_v_1 gameStateMachine) {
		super(gameStateMachine, Constants.DISPLAY_GAME_TASK_TIMEOUT);
		
	}

	@Override
	protected void onTimeout() {
		gameStateMachine.onGameDisplayTimeout();
	}

}
