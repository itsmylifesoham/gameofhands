package com.gameofhands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.smartfoxserver.v2.game.CreateSFSGameSettings;

public class GameFormatManager_normal_1_v_1 extends GameFormatManager {
	public GameFormatManager_normal_1_v_1() {
		super(Arrays.asList(SfsObjectValues.NORMAL_1_V_1_OVERS_2_WICKETS_2,
				SfsObjectValues.NORMAL_1_V_1_OVERS_3_WICKETS_2, SfsObjectValues.NORMAL_1_V_1_OVERS_5_WICKETS_2,
				SfsObjectValues.NORMAL_1_V_1_OVERS_7_WICKETS_3, SfsObjectValues.NORMAL_1_V_1_OVERS_10_WICKETS_3),
				"com.gameofhands.normal_1_v_1.RoomExtension");
	}

	@Override
	protected void setCustomCreateSFSGameSettings(CreateSFSGameSettings gameSettings, String gameFormat) {
		gameSettings.setMaxUsers(2);
		gameSettings.setMinPlayersToStartGame(2);

		Map<Object, Object> customProperties = new HashMap<>();

		// based on gameFormat pass the game specific settings
		if (gameFormat.equals(SfsObjectValues.NORMAL_1_V_1_OVERS_2_WICKETS_2)) {
			customProperties.put("overs", 2);
			customProperties.put("wickets", 2);
		} else if (gameFormat.equals(SfsObjectValues.NORMAL_1_V_1_OVERS_3_WICKETS_2)) {
			customProperties.put("overs", 3);
			customProperties.put("wickets", 2);
		} else if (gameFormat.equals(SfsObjectValues.NORMAL_1_V_1_OVERS_5_WICKETS_2)) {
			customProperties.put("overs", 5);
			customProperties.put("wickets", 2);
		} else if (gameFormat.equals(SfsObjectValues.NORMAL_1_V_1_OVERS_7_WICKETS_3)) {
			customProperties.put("overs", 7);
			customProperties.put("wickets", 3);
		} else if (gameFormat.equals(SfsObjectValues.NORMAL_1_V_1_OVERS_10_WICKETS_3)) {
			customProperties.put("overs", 10);
			customProperties.put("wickets", 3);
		}
		
		gameSettings.setRoomProperties(customProperties);
	}

}
