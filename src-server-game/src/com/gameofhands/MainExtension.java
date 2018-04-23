package com.gameofhands;

import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class MainExtension extends SFSExtension {

	@Override
	public void init() {
		trace("Hello, this is my first SFS2X Extension!");

		addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
		// Add a new Request Handler
		addRequestHandler(AppRequests.JOIN_ME.toString(), AutoJoinHandler.class);
	}

	
}
