package com.gameofhands.format_1_v_1.gamestates;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.gameofhands.ExtensionReponses;
import com.gameofhands.Keys;
import com.gameofhands.SfsObjectKeys;
import com.gameofhands.SfsObjectValues;
import com.gameofhands.format_1_v_1.GameStateMachine_format_1_v_1;
import com.gameofhands.format_1_v_1.GameState_format_1_v_1;
import com.gameofhands.format_1_v_1.timeouts.TossInputTimeout;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class GameTossState extends GameState_format_1_v_1 {

	private TossInputTimeout tossInputTimeout;
	private Set<String> uniqueTossInputPlayers = new HashSet<>();

	public GameTossState(GameStateMachine_format_1_v_1 gameStateMachine) {
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
		user.setProperty(Keys.TOSS_INPUT, n);
		uniqueTossInputPlayers.add(user.getName());

		if (uniqueTossInputPlayers.size() == gameStateMachine.gameExtension.getParentRoom().getMaxUsers()) {
			int sum = 0;
			List<User> players = gameStateMachine.gameExtension.getParentRoom().getPlayersList();
			for (User player : players) {
				sum += (int) player.getProperty(Keys.TOSS_INPUT);

			}

			User tossWinner = players.get(0);
			if (players.get(0).getProperty(Keys.TOSS_PARITY).equals(SfsObjectValues.EVEN) && sum % 2 == 0) {
				tossWinner = players.get(0);
			} else {
				tossWinner = players.get(1);
			}

			
			changeState(new TossResultState(gameStateMachine, tossWinner));
		}

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
		tossInputTimeout.cancel();
	}

	@Override
	public void initialize() {
		sendGameTossCommand();
		tossInputTimeout = new TossInputTimeout(gameStateMachine);
		tossInputTimeout.start();
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

		p1.setProperty(Keys.TOSS_PARITY, SfsObjectValues.EVEN);
		ISFSObject params1 = new SFSObject();
		params1.putUtfString(SfsObjectKeys.TOSS_PARITY, SfsObjectValues.EVEN);
		gameStateMachine.gameExtension.send(ExtensionReponses.TOSS_START, params1, p1);

		p2.setProperty(Keys.TOSS_PARITY, SfsObjectValues.ODD);
		ISFSObject params2 = new SFSObject();
		params2.putUtfString(SfsObjectKeys.TOSS_PARITY, SfsObjectValues.ODD);
		gameStateMachine.gameExtension.send(ExtensionReponses.TOSS_START, params1, p2);
	}

	private int getRandomNumber(int min, int max) {
		Random r = new Random();
		return r.nextInt(max + 1 - min) + min;
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
