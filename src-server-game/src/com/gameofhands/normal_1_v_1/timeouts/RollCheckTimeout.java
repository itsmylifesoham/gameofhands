package com.gameofhands.normal_1_v_1.timeouts;

import com.gameofhands.Constants;
import com.gameofhands.GameTimeout;
import com.gameofhands.normal_1_v_1.GameStateMachine_normal_1_v_1;
import com.gameofhands.normal_1_v_1.GameState_normal_1_v_1;

public class RollCheckTimeout extends GameTimeout<GameStateMachine_normal_1_v_1, GameState_normal_1_v_1> {

	public RollCheckTimeout(GameStateMachine_normal_1_v_1 gameStateMachine) {
		super(gameStateMachine, Constants.ROLL_CHECK_TASK_TIMEOUT);
	}

	@Override
	protected void onTimeout() {
		gameStateMachine.onRollCheckTimeout();
	}

}
