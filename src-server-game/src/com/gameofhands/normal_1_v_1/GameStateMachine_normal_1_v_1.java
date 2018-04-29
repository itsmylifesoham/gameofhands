package com.gameofhands.normal_1_v_1;

import com.gameofhands.GameStateMachine;
import com.gameofhands.PlayerRole;
import com.gameofhands.normal_1_v_1.gamestates.CreatedState;
import com.gameofhands.normal_1_v_1.gamestates.DestroyedState;
import com.smartfoxserver.v2.entities.User;
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
	public synchronized void destroy() {
		super.destroy();
		this.currentState = new DestroyedState(this);
	}

	@Override
	public synchronized void onDisconnected(User user) {
		this.currentState.onDisconnected(user);
	}

	@Override
	public synchronized void onQuit(User user) {
		this.currentState.onQuit(user);
	}

	@Override
	public synchronized void onUserUnresponsive(User user) {
		this.currentState.onUserUnresponsive(user);
	}

	@Override
	public synchronized void onPause(User user) {
		this.currentState.onPause(user);
	}

	@Override
	public synchronized void onPauseExpired() {
		this.currentState.onPauseExpired();
	}

	@Override
	public synchronized void onResume(User user) {
		this.currentState.onResume(user);
	}

	@Override
	public synchronized void onPlayerJoin(User user) {
		this.currentState.onPlayerJoin(user);
	}

	@Override
	public synchronized void onRollCheck(User user) {
		this.currentState.onRollCheck(user);
	}

	@Override
	public synchronized void onRollCheckTimeout() {
		this.currentState.onRollCheckTimeout();
	}

	@Override
	public synchronized void onGameLoaded(User user) {
		this.currentState.onGameLoaded(user);
	}

	@Override
	public synchronized void onGameLoadTimeout() {
		this.currentState.onGameLoadTimeout();
	}

	@Override
	public synchronized void onGameStart(User user) {
		this.currentState.onGameStart(user);
	}

	@Override
	public synchronized void onGameStartTimeout() {
		this.currentState.onGameStartTimeout();
	}

	@Override
	public synchronized void onTossInput(User user, int n) {
		this.currentState.onTossInput(user, n);
	}

	@Override
	public synchronized void onTossInputTimeout() {
		this.currentState.onTossInputTimeout();
	}

	@Override
	public synchronized void onSelectRole(User user, PlayerRole role) {
		this.currentState.onSelectRole(user, role);
	}

	@Override
	public synchronized void onSelectRoleTimeout() {
		this.currentState.onSelectRoleTimeout();
	}

	@Override
	public synchronized void onInput(User user, int num) {
		this.currentState.onInput(user, num);
	}

	@Override
	public synchronized void onInputTimeout() {
		this.currentState.onInputTimeout();
		
	}

	
}
