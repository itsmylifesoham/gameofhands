package com.gameofhands;

import com.smartfoxserver.v2.extensions.SFSExtension;

public abstract class GameStateMachine<TGameStateMachine extends GameStateMachine<TGameStateMachine, TGameState>, TGameState extends GameState<TGameStateMachine, TGameState>> {
	public TGameState currentState;
	public final SFSExtension gameExtension;

	protected GameStateMachine(SFSExtension gameExtension) {
		this.gameExtension = gameExtension;
		this.setInitialState();
	}

	public synchronized void destroy() {
		this.currentState.destroy();
	}

	protected abstract void setInitialState();
}
