package com.gameofhands;

import java.util.ArrayList;
import java.util.List;

import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class MainExtension extends SFSExtension {

	MatchingEngine matchingEngine;

	@Override
	public void init() {
		trace("Hello, this is GameOfHands extension!");

		addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
		// Add a new Request Handler
		addRequestHandler(ExtensionRequests.JOIN_ME, AutoJoinHandler.class);
		addRequestHandler(ExtensionRequests.UNJOIN_ME, UnJoinHandler.class);

		initMatchingEngine();

	}

	private void initMatchingEngine() {
		List<String> allSupportedGameFormats = new ArrayList<>();
		for (GameFormatManager manager : GameFormatManager.availableGameFormatManagers) {
			allSupportedGameFormats.addAll(manager.supportedGameFormats);
		}
		matchingEngine = new MatchingEngine(allSupportedGameFormats);

		matchingEngine.start();

	}

	@Override
	public void destroy() {
		matchingEngine.stop();
	}

}
