package com.gameofhands.normal_1_v_1;

import com.gameofhands.GameStateMachine;
import com.gameofhands.normal_1_v_1.gamestates.CreatedState;
import com.gameofhands.normal_1_v_1.gamestates.DestroyedState;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class GameStateMachine_normal_1_v_1 extends
		GameStateMachine<GameStateMachine_normal_1_v_1, GameState_normal_1_v_1> implements IEvents_normal_1_v_1 {

	public GameStateMachine_normal_1_v_1(SFSExtension gameExtension) {
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
	
	@Override
	public synchronized void destroy() {
		super.destroy();
		this.currentState = new DestroyedState(this);
	}
}
