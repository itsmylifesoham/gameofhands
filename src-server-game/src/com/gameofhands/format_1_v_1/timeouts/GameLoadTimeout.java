package com.gameofhands.format_1_v_1.timeouts;

import com.gameofhands.Constants;
import com.gameofhands.GameTimeout;
import com.gameofhands.format_1_v_1.GameStateMachine_format_1_v_1;
import com.gameofhands.format_1_v_1.GameState_format_1_v_1;

public class GameLoadTimeout extends GameTimeout<GameStateMachine_format_1_v_1, GameState_format_1_v_1> {

	public GameLoadTimeout(GameStateMachine_format_1_v_1 gameStateMachine) {
		super(gameStateMachine, Constants.GAME_LOAD_TASK_TIMEOUT);
	}

	@Override
	protected void onTimeout() {
		gameStateMachine.onGameLoadTimeout();
	}

}
