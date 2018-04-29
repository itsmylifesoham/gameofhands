package com.gameofhands.normal_1_v_1;

import com.gameofhands.GameState;
import com.gameofhands.GameStateMachine;

public abstract class GameState_normal_1_v_1 extends GameState<GameStateMachine_normal_1_v_1> implements IEvents_normal_1_v_1 {

	protected GameState_normal_1_v_1(GameStateMachine_normal_1_v_1 gameStateMachine) {
		super(gameStateMachine);
	}	
}
