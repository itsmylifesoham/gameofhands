package com.gameofhands.format_1_v_1.gamestates;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gameofhands.ExtensionReponses;
import com.gameofhands.Keys;
import com.gameofhands.SfsObjectKeys;
import com.gameofhands.format_1_v_1.GameStateMachine_format_1_v_1;
import com.gameofhands.format_1_v_1.GameState_format_1_v_1;
import com.gameofhands.format_1_v_1.timeouts.GameBeginTimeout;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class RoleSelectedState extends GameState_format_1_v_1 {

	private User tossWinner;
	private String roleSelected;
	private GameBeginTimeout gameBeginTimeout;
	Set<String> uniquePlayersWithGameBeginFlag = new HashSet<>();
	
	public RoleSelectedState(GameStateMachine_format_1_v_1 gameStateMachine, User tossWinner, String roleSelected) {
		super(gameStateMachine);
		this.tossWinner = tossWinner;
		this.roleSelected = roleSelected;
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
	public void onGameBegin(User user) {
		uniquePlayersWithGameBeginFlag.add(user.getName());
		if(uniquePlayersWithGameBeginFlag.size() == gameStateMachine.gameExtension.getParentRoom().getMaxUsers())
		{
			// launch into GamePlayState
			List<User> players = gameStateMachine.gameExtension.getParentRoom().getPlayersList();
			
			Map<User, String> playerRoleMap = new HashMap<>();
			playerRoleMap.put(players.get(0), (String)players.get(0).getProperty(Keys.PLAYER_ROLE));
			playerRoleMap.put(players.get(1), (String)players.get(1).getProperty(Keys.PLAYER_ROLE));
			
			players.get(0).removeProperty(Keys.PLAYER_ROLE);
			players.get(1).removeProperty(Keys.PLAYER_ROLE);
			
			changeState(new GamePlayState(gameStateMachine, playerRoleMap));
		}
	}

	@Override
	public void onGameBeginTimeout() {
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
		gameBeginTimeout.cancel();
	}

	@Override
	public void initialize() {
		sendRoleSelectedNotificationToLoser();
		gameBeginTimeout = new GameBeginTimeout(gameStateMachine);
		gameBeginTimeout.start();
		
	}

	private void sendRoleSelectedNotificationToLoser() {
		List<User> players  = gameStateMachine.gameExtension.getParentRoom().getPlayersList();
		User tossLoser = tossWinner == players.get(0)? players.get(1) : players.get(0);
		
		ISFSObject params = new SFSObject();
		params.putUtfString(SfsObjectKeys.PLAYER_ROLE, roleSelected);
		gameStateMachine.gameExtension.send(ExtensionReponses.ROLE_SELECTED	, params, tossLoser);
	}

}
