package com.gameofhands.normal_1_v_1.gamestates;

import com.gameofhands.normal_1_v_1.GameStateMachine_normal_1_v_1;
import com.gameofhands.normal_1_v_1.GameState_normal_1_v_1;

public class RollCheckSuccess extends GameState_normal_1_v_1 {

	

	protected RollCheckSuccess(GameStateMachine_normal_1_v_1 gameStateMachine) {
		super(gameStateMachine);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPlayerJoin() {
		// TODO Auto-generated method stub

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
		gameStateMachine.gameExtension.trace("Roll check Success!");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
