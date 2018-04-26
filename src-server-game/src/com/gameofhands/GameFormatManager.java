package com.gameofhands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.gameofhands.normal_1_v_1.GameFormatManager_normal_1_v_1;
import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.api.CreateRoomSettings.RoomExtensionSettings;
import com.smartfoxserver.v2.api.ISFSApi;
import com.smartfoxserver.v2.api.ISFSGameApi;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.SFSRoomRemoveMode;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.exceptions.SFSJoinRoomException;
import com.smartfoxserver.v2.game.CreateSFSGameSettings;

public abstract class GameFormatManager {
	public static List<GameFormatManager> availableGameFormatManagers = Arrays
			.asList(new GameFormatManager_normal_1_v_1());

	private static Zone currentZone = SmartFoxServer.getInstance().getZoneManager().getZoneByName(Constants.ZONE_NAME);
	private static ISFSGameApi gameApi = SmartFoxServer.getInstance().getAPIManager().getGameApi();
	private static ISFSApi sfsApi = SmartFoxServer.getInstance().getAPIManager().getSFSApi();
	
	public List<String> supportedGameFormats;
	private String extensionName;
	private MatchEngine matchEngine;	

	public GameFormatManager(List<String> supportedGameFormats, String extensionName, MatchEngine matchEngine) {
		this.supportedGameFormats = supportedGameFormats;
		this.extensionName = extensionName;
		this.matchEngine = matchEngine;
	}	
	
	private void createGameRoom(RoomConfiguration roomConfiguration) throws Exception {		
		
		// create basic sfs game settings
		CreateSFSGameSettings gameSettings = new CreateSFSGameSettings();
		gameSettings.setName(UUID.randomUUID().toString());
		gameSettings.setAutoRemoveMode(SFSRoomRemoveMode.WHEN_EMPTY);
		gameSettings.setGamePublic(true);
		gameSettings.setDynamic(true);
		gameSettings.setMaxSpectators(10);
		gameSettings.setMaxVariablesAllowed(50);
		gameSettings.setGroupId(roomConfiguration.groupId);
		gameSettings.setExtension(new RoomExtensionSettings(Constants.ZONE_NAME, this.extensionName));

		this.setCustomCreateSFSGameSettings(gameSettings);
		
		// set room properties to pass the room configuration for room extension to interprete.
		Map<Object, Object> roomProperties = new HashMap<>();
		roomProperties.put(Constants.ROOM_CONFIGURATION, roomConfiguration);
		gameSettings.setRoomProperties(roomProperties);
		
		// create the room			
		Room gameRoom = gameApi.createGame(currentZone, gameSettings, null);		
		
		
		// join them in the new room
		roomConfiguration.matchConfig.usersMatched.forEach((user)-> {
			try {
				sfsApi.joinRoom(user, gameRoom, null, false, user.getLastJoinedRoom());
			} catch (SFSJoinRoomException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}	
	
	public void initiateGames() {
		for(String gf : this.supportedGameFormats) {
			List<User> usersToMatch = currentZone.getRoomByName("JOIN_ME_" + gf).getUserList();
			List<MatchConfiguration> matchConfigs = this.matchEngine.match(usersToMatch);
			
			
			
		}
	}

	public static GameFormatManager getGameFormatManager(String gameFormat) throws Exception {
		
		for (GameFormatManager gameFormatManager : availableGameFormatManagers) {
			if (gameFormatManager.isGameFormatSupported(gameFormat)) {
				return gameFormatManager;
			}
		}

		throw new Exception("No GameFormatManager available for :" + gameFormat);
	}

	protected abstract void setCustomCreateSFSGameSettings(CreateSFSGameSettings gameSettings);
}
