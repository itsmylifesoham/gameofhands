package com.gameofhands.normal_1_v_1.gamestates;

import com.gameofhands.PlayerRole;
import com.gameofhands.normal_1_v_1.GameStateMachine_normal_1_v_1;
import com.gameofhands.normal_1_v_1.GameState_normal_1_v_1;
import com.smartfoxserver.v2.entities.User;

public class RollCheckSuccess extends GameState_normal_1_v_1 {

	

	protected RollCheckSuccess(GameStateMachine_normal_1_v_1 gameStateMachine) {
		super(gameStateMachine);
		// TODO Auto-generated constructor stub
	}



	@Override
	public void initialize() {
		gameStateMachine.gameExtension.trace("Roll check Success!");
	}



	@Override
	public void onDisconnected(User user) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onQuit(User user) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onUserUnresponsive(User user) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onPause(User user) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onPauseExpired() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onResume(User user) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onPlayerJoin(User user) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onRollCheck(User user) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onRollCheckTimeout() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onGameLoaded(User user) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onGameLoadTimeout() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onGameStart(User user) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onGameStartTimeout() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onTossInput(User user, int n) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onTossInputTimeout() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onSelectRole(User user, PlayerRole role) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onSelectRoleTimeout() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onInput(User user, int num) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onInputTimeout() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	
	

}
