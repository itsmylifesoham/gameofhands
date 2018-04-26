package com.gameofhands;

import java.util.List;

import com.smartfoxserver.v2.entities.User;

public interface MatchEngine {
	public List<MatchConfiguration> match(List<User> usersToMatch);
}
