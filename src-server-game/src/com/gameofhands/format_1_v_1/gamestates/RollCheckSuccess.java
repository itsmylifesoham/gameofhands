package com.gameofhands.format_1_v_1.gamestates;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gameofhands.ExtensionReponses;
import com.gameofhands.format_1_v_1.GameStateMachine_format_1_v_1;
import com.gameofhands.format_1_v_1.GameState_format_1_v_1;
import com.gameofhands.format_1_v_1.timeouts.GameLoadTimeout;
import com.smartfoxserver.v2.entities.User;

public class RollCheckSuccess extends GameState_format_1_v_1 {
	
	GameLoadTimeout gameLoadTimeout;
	Set<String> gameLoadedUniquePlayers = new HashSet<>();
	
	protected RollCheckSuccess(GameStateMachine_format_1_v_1 gameStateMachine) {
		super(gameStateMachine);
		// TODO Auto-generated constructor stub
	}

	private void sendLoadGameCommand() {
		List<User> players = gameStateMachine.gameExtension.getParentRoom().getPlayersList();
		gameStateMachine.gameExtension.send(ExtensionReponses.LOAD_GAME, null, players);
	}

	@Override
	public void initialize() {
		gameStateMachine.gameExtension.trace("Roll check Success!");
		sendLoadGameCommand();
		gameLoadTimeout = new GameLoadTimeout(gameStateMachine);
		gameLoadTimeout.start();
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
		gameLoadedUniquePlayers.add(user.getName());
		if(gameLoadedUniquePlayers.size() == gameStateMachine.gameExtension.getParentRoom().getMaxUsers())
			changeState(new DisplayGameState(gameStateMachine));
	}

	@Override
	public void onGameLoadTimeout() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameDisplay(User user) {
		// TODO Auto-generated method stub

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
	public void onSelectRole(User user, String role) {
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
		gameLoadTimeout.cancel();
	}

	@Override
	public void onGameBegin(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameBeginTimeout() {
		// TODO Auto-generated method stub
		
	}

	

}
