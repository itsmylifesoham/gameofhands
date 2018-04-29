package com.gameofhands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.api.CreateRoomSettings.RoomExtensionSettings;
import com.smartfoxserver.v2.api.ISFSApi;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.SFSRoomRemoveMode;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
import com.smartfoxserver.v2.exceptions.SFSJoinRoomException;
import com.smartfoxserver.v2.extensions.ISFSExtension;
import com.smartfoxserver.v2.game.CreateSFSGameSettings;

public abstract class GameFormatManager {

	// data required to initiate games
	private List<String> supportedGameFormatSubCategories = new ArrayList<>();
	private String extensionName;
	private MatchEngine matchEngine;
	private Map<String, Map<String, Object>> gameFormatSubCategoryToGameDataMap;

	public GameFormatManager(Map<String, Map<String, Object>> gameFormatSubCategoryToGameDataMap, String extensionName,
			MatchEngine matchEngine) {

		this.gameFormatSubCategoryToGameDataMap = gameFormatSubCategoryToGameDataMap;
		this.extensionName = extensionName;
		this.matchEngine = matchEngine;

		populateSupportedGameSubCategories();
	}

	public void EnsureJoinRoomsAreInitialized() throws Exception {
		Zone currentZone = SmartFoxServer.getInstance().getZoneManager().getZoneByName(Constants.ZONE_NAME);
		for (String gfSubCateogry : this.supportedGameFormatSubCategories) {
			if (currentZone.getRoomByName("JOIN_ME_" + gfSubCateogry) == null) {
				throw new Exception("There is no join me room for game format : " + gfSubCateogry);
			}
		}
	}

	protected abstract void setCustomCreateSFSGameSettings(CreateSFSGameSettings gameSettings);

	public void initiateGames() {
		ISFSApi sfsApi = SmartFoxServer.getInstance().getAPIManager().getSFSApi();
		Zone currentZone = SmartFoxServer.getInstance().getZoneManager().getZoneByName(Constants.ZONE_NAME);
		for (String gfSubCategory : this.supportedGameFormatSubCategories) {
			Room currentJoinRoom = currentZone.getRoomByName("JOIN_ME_" + gfSubCategory);
			List<User> usersToMatch = currentJoinRoom.getUserList();
			List<MatchConfiguration> matchConfigurations = this.matchEngine.match(usersToMatch);

			for (MatchConfiguration matchConfiguration : matchConfigurations) {

				CreateSFSGameSettings gameSettings = createGameSettings(gfSubCategory, matchConfiguration);
				Room gameRoom = null;

				try {
					gameRoom = createGameRoom(gameSettings);
					launchUsersInGameRoom(matchConfiguration.usersMatched, gameRoom, currentJoinRoom);
				} catch (Exception e) {
					if (gameRoom != null) {
						sfsApi.removeRoom(gameRoom);
					}
				}

			}
		}
	}

	private void launchUsersInGameRoom(List<User> usersMatched, Room gameRoom, Room joinRoom) throws Exception {
		ISFSApi sfsApi = SmartFoxServer.getInstance().getAPIManager().getSFSApi();
		ISFSExtension zoneExtension = SmartFoxServer.getInstance().getZoneManager().getZoneByName(Constants.ZONE_NAME).getExtension();
		for (User user : usersMatched) {
			sfsApi.leaveRoom(user, joinRoom);
			try {				
				sfsApi.joinRoom(user, gameRoom, null, false, null);
			} catch (SFSJoinRoomException e) {
				notifyUserUnableToJoinGame(zoneExtension, user);

				// put users in game room back into join room
				gameRoom.getUserList().forEach((addedUser) -> {
					sfsApi.leaveRoom(addedUser, gameRoom);
					try {
						sfsApi.joinRoom(addedUser, joinRoom, null, false, null);
					} catch (SFSJoinRoomException e1) {
						notifyUserUnableToJoinGame(zoneExtension, addedUser);
					}
				});

				throw new Exception("One of the matched users was unable to join the game room");
			}
		}
	}

	private void notifyUserUnableToJoinGame(ISFSExtension zoneExtension, User user) {		
		zoneExtension.send(ExtensionReponses.UNABLE_TO_JOIN, null, user);
	}

	private CreateSFSGameSettings createGameSettings(String gfSubCategory, MatchConfiguration matchConfiguration) {
		CreateSFSGameSettings basicGameSettings = createBasicRoomSettings();
		basicGameSettings.setGroupId(gfSubCategory);
		this.setCustomCreateSFSGameSettings(basicGameSettings);

		RoomConfiguration roomConfiguration = new RoomConfiguration();
		roomConfiguration.gameConfigData = this.gameFormatSubCategoryToGameDataMap.get(gfSubCategory);
		roomConfiguration.matchConfiguration = matchConfiguration;
		roomConfiguration.gameFormatSubCategory = gfSubCategory;
		Map<Object, Object> roomPropertyMap = new HashMap<>();
		roomPropertyMap.put("ROOM_CONFIGURATION", roomConfiguration);

		basicGameSettings.setRoomProperties(roomPropertyMap);

		return basicGameSettings;

	}

	private Room createGameRoom(CreateSFSGameSettings gameSettings) throws SFSCreateRoomException {
		Zone currentZone = SmartFoxServer.getInstance().getZoneManager().getZoneByName(Constants.ZONE_NAME);
		return SmartFoxServer.getInstance().getAPIManager().getGameApi().createGame(currentZone, gameSettings, null);
	}

	private CreateSFSGameSettings createBasicRoomSettings() {

		CreateSFSGameSettings gameSettings = new CreateSFSGameSettings();
		gameSettings.setName(UUID.randomUUID().toString());
		gameSettings.setAutoRemoveMode(SFSRoomRemoveMode.WHEN_EMPTY);
		gameSettings.setGamePublic(true);
		gameSettings.setDynamic(true);
		gameSettings.setMaxSpectators(10);
		gameSettings.setMaxVariablesAllowed(50);
		gameSettings.setExtension(new RoomExtensionSettings(Constants.ZONE_NAME, this.extensionName));
		return gameSettings;
	}

	private void populateSupportedGameSubCategories() {
		for (String key : gameFormatSubCategoryToGameDataMap.keySet()) {
			supportedGameFormatSubCategories.add(key);
		}
	}

}
