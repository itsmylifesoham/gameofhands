package com.gameofhands;

import java.util.ArrayList;
import java.util.List;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.extensions.ExtensionLogLevel;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class MainExtension extends SFSExtension {

	MatchingEngineTemp matchingEngine;

	@Override
	public void init() {
		trace("Hello, this is GameOfHands extension!");
		
		try {
			EnsureAllRoomsForSupportedGameFormatsAreCreated();
		} catch (Exception e) {
			trace(ExtensionLogLevel.ERROR, e.getMessage());			
		}
		
		addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
		// Add a new Request Handler
		addRequestHandler(ExtensionRequests.JOIN_ME, AutoJoinHandler.class);
		addRequestHandler(ExtensionRequests.UNJOIN_ME, UnJoinHandler.class);

		initMatchingEngine();

	}
	
	
	private void EnsureAllRoomsForSupportedGameFormatsAreCreated() throws Exception {
		Zone zone = getParentZone();
		for(GameFormatManager gfManager : GameFormatManager.availableGameFormatManagers) {
			for(String gf : gfManager.supportedGameFormats) {
				if (zone.getRoomByName("JOIN_ME_" + gf) == null) {
					throw new Exception("There is no join me room for game format : " + gf);
				}
			}
		}
	}

	private void initMatchingEngine() {
		List<String> allSupportedGameFormats = new ArrayList<>();
		for (GameFormatManager manager : GameFormatManager.availableGameFormatManagers) {
			allSupportedGameFormats.addAll(manager.supportedGameFormats);
		}
		
	}

	@Override
	public void destroy() {
		matchingEngine.stop();
	}

}
