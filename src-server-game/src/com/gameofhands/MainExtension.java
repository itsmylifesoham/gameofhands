package com.gameofhands;

import java.util.Arrays;
import java.util.List;

import com.gameofhands.normal_1_v_1.GameFormatManager_normal_1_v_1;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class MainExtension extends SFSExtension {

	MatchingTaskManager matchingTaskManager;
	private List<GameFormatManager> availableGameFormatManagers = Arrays.asList(new GameFormatManager_normal_1_v_1());

	@Override
	public void init() {
		trace("Hello, this is GameOfHands extension!");
		addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
		initMatchingTask();
	}

	private void initMatchingTask() {
		matchingTaskManager = new MatchingTaskManager();
		matchingTaskManager.start();
	}

	public void initiateAllGames() {
		availableGameFormatManagers.forEach((gfManager) -> gfManager.initiateGames());
	}

	public void EnsureAllJoinRoomsAreInitialized() throws Exception {

		for (GameFormatManager gameFormatManager : availableGameFormatManagers) {
			gameFormatManager.EnsureJoinRoomsAreInitialized();
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		matchingTaskManager.stop();
	}

}
