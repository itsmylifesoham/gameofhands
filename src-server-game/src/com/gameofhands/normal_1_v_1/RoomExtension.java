package com.gameofhands.normal_1_v_1;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import com.gameofhands.Constants;
import com.gameofhands.ExtensionReponses;
import com.gameofhands.ExtensionRequests;
import com.gameofhands.SfsObjectKeys;
import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class RoomExtension extends SFSExtension {

	private AtomicReference<GameState> gameState = new AtomicReference<GameState>(GameState.CREATED);
	public ConcurrentLinkedQueue<User> rollCheckSuccessUsers = new ConcurrentLinkedQueue<>();

	private class RollCheckTimeoutTaskRunner implements Runnable {

		public void run() {
			try {
				setGameState(GameState.ROLL_CHECK_FAILED);
			} catch (Exception e) {

			}
		}
	}

	@Override
	public void init() {
		trace("Game Room created yay!!");
		addEventHandler(SFSEventType.USER_JOIN_ROOM, UserJoinGameRoomHandler.class);
		
	}

	@Override
	public void destroy() {
		super.destroy();

		if (rollCheckTaskHandle != null) {
			rollCheckTaskHandle.cancel(true);
		}
	}

	public void setGameState(GameState newGameState) {

		switch (newGameState) {
		case FILLED:
			if (!gameState.compareAndSet(GameState.CREATED, GameState.FILLED)) {
				return;
			}
			
			setGameState(GameState.DISPLAY_MATCH);
			break;
			
		case DISPLAY_MATCH:
			if (!gameState.compareAndSet(GameState.FILLED, GameState.DISPLAY_MATCH)) {
				return;
			}
			
			sendDisplayMatchToPlayers();
			setGameState(GameState.ROLL_CHECK);
			break;
			
		case ROLL_CHECK:
			if (!gameState.compareAndSet(GameState.DISPLAY_MATCH, GameState.ROLL_CHECK)) {
				return;
			}
			
			getRollCheck();
			break;
			
		case ROLL_CHECK_SUCCESS:
			if (!gameState.compareAndSet(GameState.ROLL_CHECK, GameState.ROLL_CHECK_SUCCESS)) {
				return;
			}
			
			removeRequestHandler(ExtensionRequests.ROLL_CHECK);
			trace("Roll check success!!!---------------------");
			rollCheckTaskHandle.cancel(true);
			break;
			
		case ROLL_CHECK_FAILED:
			if (!gameState.compareAndSet(GameState.ROLL_CHECK, GameState.ROLL_CHECK_FAILED)) {
				return;
			}
			
			removeRequestHandler(ExtensionRequests.ROLL_CHECK);
			trace("Roll check failed!!!---------------------");
			break;
		default:
			break;
		}

	}

	// Keeps a reference to the task execution
	public ScheduledFuture<?> rollCheckTaskHandle;

	private void getRollCheck() {
		addRequestHandler(ExtensionRequests.ROLL_CHECK, RollCheckHandler.class);
		Room gameRoom = getParentRoom();
		List<User> playersInRoom = gameRoom.getPlayersList();

		getApi().sendExtensionResponse(ExtensionReponses.ROLL_CHECK, null, playersInRoom, gameRoom, false);

		// schedule roll check timeout
		SmartFoxServer sfs = SmartFoxServer.getInstance();
		rollCheckTaskHandle = sfs.getTaskScheduler().schedule(new RollCheckTimeoutTaskRunner(),
				Constants.ROLL_CHECK_TASK_TIMEOUT, TimeUnit.SECONDS);

	}

	private void sendDisplayMatchToPlayers() {

		List<User> playersInRoom = getParentRoom().getPlayersList();

		ISFSObject matchData1 = new SFSObject();
		matchData1.putUtfString(SfsObjectKeys.USER_LOGIN_ID, playersInRoom.get(0).getName());

		send(ExtensionReponses.DISPLAY_MATCH, matchData1, playersInRoom.get(1));

		ISFSObject matchData2 = new SFSObject();
		matchData2.putUtfString(SfsObjectKeys.USER_LOGIN_ID, playersInRoom.get(1).getName());

		send(ExtensionReponses.DISPLAY_MATCH, matchData2, playersInRoom.get(0));
	}

}
