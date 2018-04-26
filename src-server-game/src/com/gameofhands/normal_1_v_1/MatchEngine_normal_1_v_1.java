package com.gameofhands.normal_1_v_1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gameofhands.MatchConfiguration;
import com.gameofhands.MatchEngine;
import com.smartfoxserver.v2.entities.User;

public class MatchEngine_normal_1_v_1 implements MatchEngine {

	@Override
	public List<MatchConfiguration> match(List<User> usersToMatch) {
		
		List<MatchConfiguration> result = new ArrayList<>();
		
		int lengthToMatch = usersToMatch.size()%2 == 0 ? usersToMatch.size():usersToMatch.size()-1;
		for (int i = 0; i < lengthToMatch; i += 2) {
			Map<String, Object> data = new HashMap<>();
			data.put("player1", usersToMatch.get(i));
			data.put("player2", usersToMatch.get(i+1));
			
			result.add(new MatchConfiguration(data));
		}
		
		return result;		
	}
	
}
