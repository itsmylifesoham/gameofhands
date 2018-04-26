package com.gameofhands;

import java.util.Map;

public class RoomConfiguration {
	public Map<String, Object> data;
	public String groupId;
	public MatchConfiguration matchConfig;
	
	
	public RoomConfiguration(Map<String, Object> data, String groupId, MatchConfiguration matchConfig) {
		this.data = data;
		this.groupId = groupId;
		this.matchConfig = matchConfig;
	}
}
