package com.gameofhands;

import com.smartfoxserver.v2.extensions.SFSExtension;

public abstract class GameStateMachine<TGameState extends GameState, TGameExtension extends SFSExtension> {
	public TGameState currentState;	
	public final TGameExtension gameExtension;
	
	protected GameStateMachine(TGameExtension gameExtension) {
		this.gameExtension = gameExtension;		
		this.setInitialState();
	}
	
	public void destroy() {
		this.currentState.destroy();
	}
	
	protected abstract void setInitialState();
}	
