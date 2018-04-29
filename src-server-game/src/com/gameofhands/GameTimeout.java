package com.gameofhands;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.smartfoxserver.v2.SmartFoxServer;

public abstract class GameTimeout<TGameStateMachine extends GameStateMachine<TGameStateMachine, TGameState>, TGameState extends GameState<TGameStateMachine, TGameState>> {
	// Keeps a reference to the task execution
	public ScheduledFuture<?> timeoutTaskHandle;
	protected TGameStateMachine gameStateMachine;
	private int timeoutDuration;
	
	public GameTimeout(TGameStateMachine gameStateMachine, int timeoutDuration) {
		this.gameStateMachine = gameStateMachine;
		this.timeoutDuration = timeoutDuration;
	}
	
	private class TimeoutTaskRunner implements Runnable {

		public void run() {
			try {
				onTimeout();
			} catch (Exception e) {

			}
		}
	}

	public void start() {
		// schedule roll check timeout
		SmartFoxServer sfs = SmartFoxServer.getInstance();
		timeoutTaskHandle = sfs.getTaskScheduler().schedule(new TimeoutTaskRunner(),
				timeoutDuration, TimeUnit.SECONDS);
	}

	public void cancel() {
		if(timeoutTaskHandle != null)
			timeoutTaskHandle.cancel(true);
	}

	protected abstract void onTimeout();
}
