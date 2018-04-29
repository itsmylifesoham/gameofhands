package com.gameofhands;

public abstract class GameState<TGameStateMachine extends GameStateMachine> {

	public TGameStateMachine gameStateMachine;

	protected GameState(TGameStateMachine gameStateMachine) {
		this.gameStateMachine = gameStateMachine;
		initialize();
	}

	public abstract void destroy();

	protected void changeState(GameState<TGameStateMachine> newState) {
		this.gameStateMachine.currentState = newState;
		this.destroy();
	}

	public abstract void initialize();

}
