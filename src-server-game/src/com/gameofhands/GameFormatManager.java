package com.gameofhands;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.api.CreateRoomSettings.RoomExtensionSettings;
import com.smartfoxserver.v2.api.ISFSApi;
import com.smartfoxserver.v2.api.ISFSGameApi;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.SFSRoomRemoveMode;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.game.CreateSFSGameSettings;

public abstract class GameFormatManager {
	private static List<GameFormatManager> availableGameFormatManagers = Arrays
			.asList(new GameFormatManager_normal_1_v_1());

	private List<String> supportedGameFormats;
	private String extensionName;

	public boolean isGameFormatSupported(String gameFormat) {
		return supportedGameFormats.indexOf(gameFormat) != -1;
	}

	public GameFormatManager(List<String> supportedGameFormats, String extensionName) {
		this.supportedGameFormats = supportedGameFormats;
		this.extensionName = extensionName;
	}

	public Room createGameRoom(String gameFormat) throws Exception {
		if (!this.isGameFormatSupported(gameFormat)) {
			throw new Exception("provided game format:" + gameFormat + "is not supported by this game format manager");
		}

		CreateSFSGameSettings gameSettings = new CreateSFSGameSettings();
		gameSettings.setName(UUID.randomUUID().toString());
		gameSettings.setAutoRemoveMode(SFSRoomRemoveMode.WHEN_EMPTY);
		gameSettings.setGamePublic(true);
		gameSettings.setDynamic(true);
		gameSettings.setMaxSpectators(10);
		gameSettings.setMaxVariablesAllowed(50);
		gameSettings.setGroupId(gameFormat);
		gameSettings.setExtension(new RoomExtensionSettings(Constants.ZONE_NAME, this.extensionName));

		this.setCustomCreateSFSGameSettings(gameSettings, gameFormat);

		ISFSGameApi gameApi = SmartFoxServer.getInstance().getAPIManager().getGameApi();
		Zone currentZone = SmartFoxServer.getInstance().getZoneManager().getZoneByName(Constants.ZONE_NAME);
		return gameApi.createGame(currentZone, gameSettings, null);
	}

	public void createGameRoomAndAddUser(User firstUser, String gameFormat) throws Exception {
		Room gameRoom = this.createGameRoom(gameFormat);
		ISFSApi gameApi = SmartFoxServer.getInstance().getAPIManager().getSFSApi();
		gameApi.joinRoom(firstUser, gameRoom);
	}

	public static GameFormatManager getGameFormatManager(String gameFormat) throws Exception {
		
		for (GameFormatManager gameFormatManager : availableGameFormatManagers) {
			if (gameFormatManager.isGameFormatSupported(gameFormat)) {
				return gameFormatManager;
			}
		}

		throw new Exception("No GameFormatManager available for :" + gameFormat);
	}

	protected abstract void setCustomCreateSFSGameSettings(CreateSFSGameSettings gameSettings, String gameFormat);
}
