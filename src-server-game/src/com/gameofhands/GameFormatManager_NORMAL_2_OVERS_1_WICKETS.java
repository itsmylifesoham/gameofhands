package com.gameofhands;

import com.smartfoxserver.v2.game.CreateSFSGameSettings;

public class GameFormatManager_NORMAL_2_OVERS_1_WICKETS extends GameFormatManager {
	public GameFormatManager_NORMAL_2_OVERS_1_WICKETS() {
		super(GameFormats.NORMAL_2_OVERS_1_WICKET);
	}
	
	@Override
	protected void setCustomCreateSFSGameSettings(CreateSFSGameSettings gameSettings) {
		gameSettings.setMaxUsers(2);
		gameSettings.setMinPlayersToStartGame(2);
	}

}
