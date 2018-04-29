package com.gameofhands.normal_1_v_1.gamestates;

import java.util.List;

import com.gameofhands.ExtensionReponses;
import com.gameofhands.SfsObjectKeys;
import com.gameofhands.normal_1_v_1.GameStateMachine_normal_1_v_1;
import com.gameofhands.normal_1_v_1.GameState_normal_1_v_1;
import com.gameofhands.normal_1_v_1.timeouts.RollCheckTimeout;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class DisplayMatchState extends GameState_normal_1_v_1 {
	RollCheckTimeout rollChecktimeout;

	protected DisplayMatchState(GameStateMachine_normal_1_v_1 gameStateMachine) {
		super(gameStateMachine);
		// TODO Auto-generated constructor stub
	}

	private int rollCheckedUserCount = 0;

	@Override
	public void onPlayerJoin() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSingleRollCheck() {

		rollCheckedUserCount++;
		if (rollCheckedUserCount == gameStateMachine.gameExtension.getParentRoom().getMaxUsers()) {
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

}
