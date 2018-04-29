package com.gameofhands.normal_1_v_1.gamestates;

import com.gameofhands.normal_1_v_1.GameStateMachine_normal_1_v_1;
import com.gameofhands.normal_1_v_1.GameState_normal_1_v_1;

public class CreatedState extends GameState_normal_1_v_1 {

	public CreatedState(GameStateMachine_normal_1_v_1 gameStateMachine) {
		super(gameStateMachine);
	}

	int playersJoined = 0;

	@Override
	public void onPlayerJoin() {

		playersJoined++;
		if (playersJoined == gameStateMachine.gameExtension.getParentRoom().getMaxUsers()) {
			changeState(new DisplayMatchState(gameStateMachine));
		}
	}

	@Override
	public void onSingleRollCheck() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRollCheckTimeout() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
