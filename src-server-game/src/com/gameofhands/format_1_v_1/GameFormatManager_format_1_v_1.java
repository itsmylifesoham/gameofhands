package com.gameofhands.format_1_v_1;

import java.util.HashMap;
import java.util.Map;

import com.gameofhands.Constants;
import com.gameofhands.GameFormatManager;
import com.gameofhands.SfsObjectValues;
import com.smartfoxserver.v2.game.CreateSFSGameSettings;

public class GameFormatManager_format_1_v_1 extends GameFormatManager {
	
	private static Map<String, Map<String, Object>> gameFormatSubCategoryToGameDataMap = new HashMap<>();
	
	static {
		gameFormatSubCategoryToGameDataMap.put(SfsObjectValues.NORMAL_1_V_1_OVERS_2_WICKETS_2, getGameDataMap(2,2));
		gameFormatSubCategoryToGameDataMap.put(SfsObjectValues.NORMAL_1_V_1_OVERS_3_WICKETS_2, getGameDataMap(3,2));
		gameFormatSubCategoryToGameDataMap.put(SfsObjectValues.NORMAL_1_V_1_OVERS_5_WICKETS_2, getGameDataMap(5,2));
		gameFormatSubCategoryToGameDataMap.put(SfsObjectValues.NORMAL_1_V_1_OVERS_7_WICKETS_3, getGameDataMap(7,3));
		gameFormatSubCategoryToGameDataMap.put(SfsObjectValues.NORMAL_1_V_1_OVERS_10_WICKETS_3, getGameDataMap(10,3));
		
		gameFormatSubCategoryToGameDataMap.put(SfsObjectValues.UNLIMITED_1_V_1_OVERS_2, getGameDataMap(2, Integer.MAX_VALUE, Constants.WICKET_PENALTY));
		gameFormatSubCategoryToGameDataMap.put(SfsObjectValues.UNLIMITED_1_V_1_OVERS_3, getGameDataMap(3, Integer.MAX_VALUE, Constants.WICKET_PENALTY));
		gameFormatSubCategoryToGameDataMap.put(SfsObjectValues.UNLIMITED_1_V_1_OVERS_5, getGameDataMap(5, Integer.MAX_VALUE, Constants.WICKET_PENALTY));
		gameFormatSubCategoryToGameDataMap.put(SfsObjectValues.UNLIMITED_1_V_1_OVERS_7, getGameDataMap(7, Integer.MAX_VALUE, Constants.WICKET_PENALTY));
		gameFormatSubCategoryToGameDataMap.put(SfsObjectValues.UNLIMITED_1_V_1_OVERS_10, getGameDataMap(10, Integer.MAX_VALUE, Constants.WICKET_PENALTY));
		
		gameFormatSubCategoryToGameDataMap.put(SfsObjectValues.UNLIMITED_1_V_1_WICKETS_1, getGameDataMap(Integer.MAX_VALUE,1));
		gameFormatSubCategoryToGameDataMap.put(SfsObjectValues.UNLIMITED_1_V_1_WICKETS_2, getGameDataMap(Integer.MAX_VALUE,2));
		gameFormatSubCategoryToGameDataMap.put(SfsObjectValues.UNLIMITED_1_V_1_OVERS_3, getGameDataMap(Integer.MAX_VALUE,3));
		gameFormatSubCategoryToGameDataMap.put(SfsObjectValues.UNLIMITED_1_V_1_WICKETS_5, getGameDataMap(Integer.MAX_VALUE,5));
	}
	
	private static Map<String, Object> getGameDataMap(int numOvers, int numWickets, int wicketPenalty){
		Map<String, Object> result = new HashMap<>();
		result.put("overs", numOvers);
		result.put("wickets", numWickets);
		result.put("wicketPenalty", wicketPenalty);
		return result;		
	}
	
	private static Map<String, Object> getGameDataMap(int numOvers, int numWickets){
		return 	getGameDataMap(numOvers, numWickets, 0);
	}
	public GameFormatManager_format_1_v_1() {
		super(gameFormatSubCategoryToGameDataMap,"com.gameofhands.normal_1_v_1.RoomExtension", new MatchEngine_format_1_v_1());
	}

	@Override
	protected void setCustomCreateSFSGameSettings(CreateSFSGameSettings gameSettings) {
		// in case of game rooms this is the max num of players, so we can still have spectators
		gameSettings.setMaxUsers(2);
		gameSettings.setMinPlayersToStartGame(2);
	}

}
