package com.gameofhands.format_1_v_1.gamestates;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gameofhands.ExtensionReponses;
import com.gameofhands.SfsObjectKeys;
import com.gameofhands.format_1_v_1.GameStateMachine_format_1_v_1;
import com.gameofhands.format_1_v_1.GameState_format_1_v_1;
import com.gameofhands.format_1_v_1.timeouts.RollCheckTimeout;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class DisplayMatchState extends GameState_format_1_v_1 {
	RollCheckTimeout rollChecktimeout;

	protected DisplayMatchState(GameStateMachine_format_1_v_1 gameStateMachine) {
		super(gameStateMachine);
		// TODO Auto-generated constructor stub
	}

	private Set<String> uniqueRollCheckedUserLoginIds = new HashSet<>();

	@Override
	public void onRollCheck(User user) {

		uniqueRollCheckedUserLoginIds.add(user.getName());
		if (uniqueRollCheckedUserLoginIds.size() == gameStateMachine.gameExtension.getParentRoom().getMaxUsers()) {
			changeState(new RollCheckSuccess(gameStateMachine));
		}

	}

	@Override
	public void onRollCheckTimeout() {
		changeState(new RollCheckFailed(gameStateMachine));
	}

	@Override
	public void initialize() {
		sendDisplayMatchToPlayers();
		rollChecktimeout = new RollCheckTimeout(gameStateMachine);
		rollChecktimeout.start();
	}

	private void sendDisplayMatchToPlayers() {

		List<User> playersInRoom = gameStateMachine.gameExtension.getParentRoom().getPlayersList();

		ISFSObject matchData1 = new SFSObject();
		matchData1.putUtfString(SfsObjectKeys.USER_LOGIN_ID, playersInRoom.get(0).getName());

		gameStateMachine.gameExtension.send(ExtensionReponses.DISPLAY_MATCH, matchData1, playersInRoom.get(1));

		ISFSObject matchData2 = new SFSObject();
		matchData2.putUtfString(SfsObjectKeys.USER_LOGIN_ID, playersInRoom.get(1).getName());

		gameStateMachine.gameExtension.send(ExtensionReponses.DISPLAY_MATCH, matchData2, playersInRoom.get(0));
	}

	@Override
	public void destroy() {
		rollChecktimeout.cancel();
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
	public void onInput(User user, int num) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInputTimeout() {
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

	

}
