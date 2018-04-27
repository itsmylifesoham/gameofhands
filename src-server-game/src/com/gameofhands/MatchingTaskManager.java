package com.gameofhands;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.smartfoxserver.v2.SmartFoxServer;

public class MatchingTaskManager {
	private class MatchTaskRunner implements Runnable {
		private int runningCycles = 0;
		
		public void run() {
			try {
				runningCycles++;
				MainExtension currentExtension = (MainExtension)SmartFoxServer.getInstance().getZoneManager().getZoneByName(Constants.ZONE_NAME).getExtension(); 
				currentExtension.trace("Inside the matching task. Cycle:  " + runningCycles);
				currentExtension.initiateAllGames();
				
			} catch (Exception e) {
				
			}
		}		
	}

	// Keeps a reference to the task execution
	ScheduledFuture<?> taskHandle;
	
	
	
	public void start() {
		SmartFoxServer sfs = SmartFoxServer.getInstance();

		// Schedule the task to run every second, with no initial delay
		taskHandle = sfs.getTaskScheduler().scheduleAtFixedRate(new MatchTaskRunner(), 30, 10, TimeUnit.SECONDS);
	}

	public void stop() {
		if (taskHandle != null)
			taskHandle.cancel(true);
	}
}
