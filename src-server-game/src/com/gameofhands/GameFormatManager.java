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
					notifyUsersUnableToJoinGame(matchConfiguration.usersMatched, currentJoinRoom);

					// remove any users who might have been joined into the room
					if (gameRoom != null) {
						sfsApi.removeRoom(gameRoom);
					}

					continue;
				}
			}
		}
	}

	private void launchUsersInGameRoom(List<User> usersMatched, Room gameRoom, Room joinRoom)
			throws SFSJoinRoomException {
		ISFSApi sfsApi = SmartFoxServer.getInstance().getAPIManager().getSFSApi();
		for (User user : usersMatched) {
			sfsApi.joinRoom(user, gameRoom, null, false, joinRoom);
		}
	}

	private void notifyUsersUnableToJoinGame(List<User> users, Room room) {
		ISFSApi sfsApi = SmartFoxServer.getInstance().getAPIManager().getSFSApi();
		sfsApi.sendExtensionResponse(ExtensionReponses.UNABLE_TO_JOIN, null, users, room, false);
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
