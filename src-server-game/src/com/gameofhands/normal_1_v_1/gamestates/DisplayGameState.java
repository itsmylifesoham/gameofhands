package com.gameofhands.normal_1_v_1.gamestates;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gameofhands.ExtensionReponses;
import com.gameofhands.PlayerRole;
import com.gameofhands.normal_1_v_1.GameStateMachine_normal_1_v_1;
import com.gameofhands.normal_1_v_1.GameState_normal_1_v_1;
import com.smartfoxserver.v2.entities.User;

public class DisplayGameState extends GameState_normal_1_v_1 {
	
	Set<String> uniqueGameDisplayedPlayers = new HashSet<>();
	
	public DisplayGameState(GameStateMachine_normal_1_v_1 gameStateMachine) {
		super(gameStateMachine);
	}

	private void sendDisplayGameCommand() {
		List<User> players = gameStateMachine.gameExtension.getParentRoom().getPlayersList();
		gameStateMachine.gameExtension.send(ExtensionReponses.DISPLAY_GAME, null, players);
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
	public void onGameDisplay(User user) {
		uniqueGameDisplayedPlayers.add(user.getName());
		if (uniqueGameDisplayedPlayers.size() == gameStateMachine.gameExtension.getParentRoom().getMaxUsers() ) {
			changeState(new GameTossState(gameStateMachine));			
		}
	}

	@Override
	public void onGameDisplayTimeout() {
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

	@Override
	public void initialize() {
		sendDisplayGameCommand();
	}

}
