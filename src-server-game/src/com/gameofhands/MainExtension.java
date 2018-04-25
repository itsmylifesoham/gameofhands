package com.gameofhands;

import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class MainExtension extends SFSExtension {

	@Override
	public void init() {
		trace("Hello, this is GameOfHands extension!");

		addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
		// Add a new Request Handler
		addRequestHandler(ExtensionRequests.JOIN_ME, AutoJoinHandler.class);
		addRequestHandler(ExtensionRequests.UNJOIN_ME, UnJoinHandler.class);
	}

	
}
