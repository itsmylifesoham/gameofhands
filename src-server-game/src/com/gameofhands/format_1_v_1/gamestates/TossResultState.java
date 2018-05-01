package com.gameofhands.format_1_v_1.gamestates;

import java.util.List;

import com.gameofhands.ExtensionReponses;
import com.gameofhands.Keys;
import com.gameofhands.SfsObjectKeys;
import com.gameofhands.SfsObjectValues;
import com.gameofhands.format_1_v_1.GameStateMachine_format_1_v_1;
import com.gameofhands.format_1_v_1.GameState_format_1_v_1;
import com.gameofhands.format_1_v_1.timeouts.PlayerRoleSelectTimeout;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class TossResultState extends GameState_format_1_v_1 {

	private User tossWinner;
	private PlayerRoleSelectTimeout playerRoleSelectTimeout;

	public TossResultState(GameStateMachine_format_1_v_1 gameStateMachine, User tossWinner) {
		super(gameStateMachine);
		this.tossWinner = tossWinner;
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
		if (user == tossWinner) {
			String tossWinnerRole = SfsObjectValues.BATTING;
			if (role.equals(SfsObjectValues.BOWLING)) {
				tossWinnerRole = SfsObjectValues.BOWLING;
			}

			List<User> players = gameStateMachine.gameExtension.getParentRoom().getPlayersList();
			User tossLoser = tossWinner == players.get(0) ? players.get(1) : players.get(0);
			tossWinner.setProperty(Keys.PLAYER_ROLE, tossWinnerRole);
			tossLoser.setProperty(Keys.PLAYER_ROLE,
					tossWinnerRole.equals(SfsObjectValues.BATTING) ? SfsObjectValues.BOWLING : SfsObjectValues.BATTING);

			changeState(new RoleSelectedState(gameStateMachine, tossWinner, tossWinnerRole));
		}
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
		playerRoleSelectTimeout.cancel();
	}

	@Override
	public void initialize() {
		sendTossResult();
		playerRoleSelectTimeout = new PlayerRoleSelectTimeout(gameStateMachine);
		playerRoleSelectTimeout.start();
	}

	private void sendTossResult() {
		List<User> players = gameStateMachine.gameExtension.getParentRoom().getPlayersList();

		User tossLoser = tossWinner == players.get(0) ? players.get(1) : players.get(0);

		ISFSObject params1 = new SFSObject();
		params1.putInt(SfsObjectKeys.TOSS_INPUT, (int) tossLoser.getProperty(Keys.TOSS_INPUT));
		gameStateMachine.gameExtension.send(ExtensionReponses.TOSS_RESULT, params1, tossWinner);

		ISFSObject params2 = new SFSObject();
		params2.putInt(SfsObjectKeys.TOSS_INPUT, (int) tossWinner.getProperty(Keys.TOSS_INPUT));
		gameStateMachine.gameExtension.send(ExtensionReponses.TOSS_RESULT, params2, tossLoser);

		removeTossProperties();
	}

	private void removeTossProperties() {
		List<User> players = gameStateMachine.gameExtension.getParentRoom().getPlayersList();
		for (User player : players) {
			player.removeProperty(Keys.TOSS_INPUT);
			player.removeProperty(Keys.TOSS_PARITY);
		}
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
