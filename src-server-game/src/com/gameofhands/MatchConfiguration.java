package com.gameofhands;

import java.util.List;
import java.util.Map;

import com.smartfoxserver.v2.entities.User;

public class MatchConfiguration {
	public Map<String, Object> data;
	public List<User> usersMatched;
	
	public MatchConfiguration(Map<String, Object> data, List<User> usersMatched) {
		this.data = data;
		this.usersMatched = usersMatched;
	}
}
