package com.gameofhands;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.api.ISFSApi;
import com.smartfoxserver.v2.api.ISFSGameApi;
import com.smartfoxserver.v2.api.CreateRoomSettings.RoomExtensionSettings;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.SFSRoomRemoveMode;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
import com.smartfoxserver.v2.exceptions.SFSJoinRoomException;
import com.smartfoxserver.v2.game.CreateSFSGameSettings;

public abstract class GameFormatManager {
	public GameFormats gameFormat;
	private static List<GameFormatManager> availableGameFormatManagers = Arrays
			.asList(new GameFormatManager_NORMAL_2_OVERS_1_WICKETS());

	public GameFormatManager(GameFormats gameFormat) {
		this.gameFormat = gameFormat;
	}

	public Room createGameRoom() throws SFSCreateRoomException {

		CreateSFSGameSettings gameSettings = new CreateSFSGameSettings();
		gameSettings.setName(UUID.randomUUID().toString());
		gameSettings.setAutoRemoveMode(SFSRoomRemoveMode.WHEN_EMPTY);
		gameSettings.setGamePublic(true);
		gameSettings.setDynamic(true);
		gameSettings.setMaxSpectators(10);
		gameSettings.setMaxVariablesAllowed(50);
		gameSettings.setNotifyGameStartedViaRoomVariable(true);
		gameSettings.setGroupId(this.gameFormat.name());
		gameSettings.setExtension(new RoomExtensionSettings(Constants.ZONE_NAME, "com.gameofhands." + this.gameFormat.name() + ".RoomExtension"));
		
		this.setCustomCreateSFSGameSettings(gameSettings);

		ISFSGameApi gameApi = SmartFoxServer.getInstance().getAPIManager().getGameApi();
		Zone currentZone = SmartFoxServer.getInstance().getZoneManager().getZoneByName(Constants.ZONE_NAME);
		return gameApi.createGame(currentZone, gameSettings, null);
	}

	public void createGameRoomAndAddUser(User firstUser) throws SFSCreateRoomException, SFSJoinRoomException {
		Room gameRoom = this.createGameRoom();
		ISFSApi gameApi = SmartFoxServer.getInstance().getAPIManager().getSFSApi();
		gameApi.joinRoom(firstUser, gameRoom);
	}

	public static GameFormatManager getGameFormatManager(String gameFormat) throws Exception {
		GameFormats gameFormatEnum = GameFormats.valueOf(gameFormat);
		for (GameFormatManager gameFormatManager : availableGameFormatManagers) {
			if (gameFormatManager.gameFormat == gameFormatEnum) {
				return gameFormatManager;
			}
		}
		
		throw new Exception("No GameFormatManager available for :" + gameFormat);		
	}

	protected abstract void setCustomCreateSFSGameSettings(CreateSFSGameSettings gameSettings);
}
