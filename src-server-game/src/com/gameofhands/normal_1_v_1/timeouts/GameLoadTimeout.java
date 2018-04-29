package com.gameofhands.normal_1_v_1.timeouts;

import com.gameofhands.Constants;
import com.gameofhands.GameTimeout;
import com.gameofhands.normal_1_v_1.GameStateMachine_normal_1_v_1;
import com.gameofhands.normal_1_v_1.GameState_normal_1_v_1;

public class GameLoadTimeout extends GameTimeout<GameStateMachine_normal_1_v_1, GameState_normal_1_v_1> {

	public GameLoadTimeout(GameStateMachine_normal_1_v_1 gameStateMachine) {
		super(gameStateMachine, Constants.GAME_LOAD_TASK_TIMEOUT);
	}

	@Override
	protected void onTimeout() {
		gameStateMachine.onGameLoadTimeout();
	}

}
