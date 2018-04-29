package com.gameofhands.normal_1_v_1.gamestates;

import java.util.List;
import java.util.Random;

import com.gameofhands.ExtensionReponses;
import com.gameofhands.PlayerRole;
import com.gameofhands.SfsObjectKeys;
import com.gameofhands.SfsObjectValues;
import com.gameofhands.normal_1_v_1.GameStateMachine_normal_1_v_1;
import com.gameofhands.normal_1_v_1.GameState_normal_1_v_1;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class GameTossState extends GameState_normal_1_v_1 {

	public GameTossState(GameStateMachine_normal_1_v_1 gameStateMachine) {
		super(gameStateMachine);
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
		sendGameTossCommand();
	}

	private void sendGameTossCommand() {
		List<User> players = gameStateMachine.gameExtension.getParentRoom().getPlayersList();
		User p1 = players.get(0);
		User p2 = players.get(1);
		
		// swap if its even
		if (getRandomNumber(1, 1000) % 2 == 0) {
			User temp = p1;
			p1 = p2;
			p2 = temp;
		}

		ISFSObject params1 = new SFSObject();
		params1.putUtfString(SfsObjectKeys.TOSS_PARITY, SfsObjectValues.EVEN);
		gameStateMachine.gameExtension.send(ExtensionReponses.TOSS_START, params1, p1);

		ISFSObject params2 = new SFSObject();
		params2.putUtfString(SfsObjectKeys.TOSS_PARITY, SfsObjectValues.ODD);
		gameStateMachine.gameExtension.send(ExtensionReponses.TOSS_START, params1, p2);
	}

	private int getRandomNumber(int min, int max) {
		Random r = new Random();
		return r.nextInt(max + 1 - min) + min;
	}

}
