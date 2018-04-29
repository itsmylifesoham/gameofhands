package com.gameofhands.normal_1_v_1;

import com.gameofhands.PlayerRole;
import com.smartfoxserver.v2.entities.User;

public interface IEvents_normal_1_v_1 {

	public void onDisconnected(User user); // when network disconnection occurs

	public void onQuit(User user); // when user willingly quits
	
	public void onUserUnresponsive(User user); // useful when the user's game might have hanged

	public void onPause(User user);
	
	public void onPauseExpired(); // when pause duration alloted per user expires

	public void onResume(User user);

	public void onPlayerJoin(User user); 

	public void onRollCheck(User user);

	public void onRollCheckTimeout();

	public void onGameLoaded(User user);

	public void onGameLoadTimeout();
	
	public void onGameStart(User user);
	
	public void onGameStartTimeout();
	
	public void onTossInput(User user, int n);
	
	public void onTossInputTimeout();	
	
	public void onSelectRole(User user, PlayerRole role);
	
	public void onSelectRoleTimeout();

	public void onInput(User user, int num); // this input is reserved for scoring actions in the game	

	public void onInputTimeout();	
}
