package com.gameofhands.format_1_v_1.timeouts;

import com.gameofhands.Constants;
import com.gameofhands.GameTimeout;
import com.gameofhands.format_1_v_1.GameStateMachine_format_1_v_1;
import com.gameofhands.format_1_v_1.GameState_format_1_v_1;

public class RollCheckTimeout extends GameTimeout<GameStateMachine_format_1_v_1, GameState_format_1_v_1> {

	public RollCheckTimeout(GameStateMachine_format_1_v_1 gameStateMachine) {
		super(gameStateMachine, Constants.ROLL_CHECK_TASK_TIMEOUT);
	}

	@Override
	protected void onTimeout() {
		gameStateMachine.onRollCheckTimeout();
	}

}
