package com.gameofhands.normal_1_v_1;

import com.gameofhands.GameStateMachine;
import com.gameofhands.normal_1_v_1.gamestates.CreatedState;

public class GameStateMachine_normal_1_v_1 extends GameStateMachine<GameState_normal_1_v_1, RoomExtension> implements IEvents_normal_1_v_1{
	
	public GameStateMachine_normal_1_v_1(RoomExtension gameExtension) {
		super(gameExtension);		
	}

	@Override
	protected void setInitialState() {
		this.currentState = new CreatedState(this);
	}

	@Override
	public synchronized void onPlayerJoin() {
		this.currentState.onPlayerJoin();
	}

	@Override
	public synchronized void onSingleRollCheck() {
		this.currentState.onSingleRollCheck();
		
	}

	@Override
	public synchronized void onRollCheckTimeout() {
		this.currentState.onRollCheckTimeout();
	}
}
