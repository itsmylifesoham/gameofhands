package com.gameofhands;

public abstract class GameState<TGameStateMachine extends GameStateMachine<TGameStateMachine, TGameState>, TGameState extends GameState<TGameStateMachine, TGameState>> {

	public TGameStateMachine gameStateMachine;

	public GameState(TGameStateMachine gameStateMachine) {
		this.gameStateMachine = gameStateMachine;
		initialize();
	}


	protected void changeState(TGameState newState) {
		this.gameStateMachine.currentState = newState;
		this.destroy();
	}

	public abstract void destroy();

	public abstract void initialize();

}
