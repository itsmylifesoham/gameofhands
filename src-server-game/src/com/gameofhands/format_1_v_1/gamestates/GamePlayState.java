package com.gameofhands.format_1_v_1.gamestates;

import java.util.Map;

import com.gameofhands.RoomConfiguration;
import com.gameofhands.format_1_v_1.GameStateMachine_format_1_v_1;
import com.gameofhands.format_1_v_1.GameState_format_1_v_1;
import com.smartfoxserver.v2.entities.User;

public class GamePlayState extends GameState_format_1_v_1 {

	private Map<User, String> playerRoleMap;
	RoomConfiguration roomConfiguration;
	GameConfigData gameConfigData;
	
	private class GameConfigData{
	
		public int overs;
		public int wickets;
		public int wicketPenalty;

		public GameConfigData(int overs, int wickets, int wicketPenalty) {
			this.overs = overs;
			this.wickets = wickets;
			this.wicketPenalty = wicketPenalty;
			
		}
	}
	public GamePlayState(GameStateMachine_format_1_v_1 gameStateMachine, Map<User, String> playerRoleMap) {
		super(gameStateMachine);
		this.playerRoleMap = playerRoleMap;
		
		roomConfiguration = (RoomConfiguration)gameStateMachine.gameExtension.getParentRoom().getProperty("ROOM_CONFIGURATION");
		gameConfigData = initGameConfigData(roomConfiguration.gameConfigData);
	}

	private GameConfigData initGameConfigData(Map<String, Object> gameConfigDataMap) {
		GameConfigData result= new GameConfigData((int)gameConfigDataMap.get("overs"), (int)gameConfigDataMap.get("wickets"), (int)gameConfigDataMap.get("wicketPenalty"));
		return result;
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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

}
