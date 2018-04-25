package com.gameofhands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class MatchingEngine {
	private class MatchTaskRunner implements Runnable {
		private int runningCycles = 0;

		public void run() {
			try {
				runningCycles++;
				currentExtension.trace("Inside the matching task. Cycle:  " + runningCycles);
				for(String gameFormat : allSupportedGameFormats) {
					processGameFormatMatching(gameFormat);
				}
				
				
			} catch (Exception e) {
				// Handle exceptions here
			}
		}

		private void processGameFormatMatching(String gameFormat) {
			List<User> usersToMatch = currentExtension.getParentZone().getRoomByName("JOIN_ME_" + gameFormat).getUserList();
			
			int lengthToMatch = usersToMatch.size()%2 == 0? usersToMatch.size(): usersToMatch.size()-1;
			for (int i = 0; i < lengthToMatch; i+=2) {
				GameFormatManager.getGameFormatManager(gameFormat).createGameRoomAndAddUsers(userList, gameFormat);
			}
			
		}
	}

	// Keeps a reference to the task execution
	ScheduledFuture<?> taskHandle;
	SFSExtension currentExtension = (SFSExtension) SmartFoxServer.getInstance().getZoneManager()
			.getZoneByName(Constants.ZONE_NAME).getExtension();
	private List<String> allSupportedGameFormats;

	public MatchingEngine(List<String> allSupportedGameFormats) {
		this.allSupportedGameFormats = allSupportedGameFormats;
	}

	public void start() {
		SmartFoxServer sfs = SmartFoxServer.getInstance();

		// Schedule the task to run every second, with no initial delay
		taskHandle = sfs.getTaskScheduler().scheduleAtFixedRate(new MatchTaskRunner(), 0, 10, TimeUnit.SECONDS);
	}

	public void stop() {
		if (taskHandle != null)
			taskHandle.cancel(true);
	}
}
