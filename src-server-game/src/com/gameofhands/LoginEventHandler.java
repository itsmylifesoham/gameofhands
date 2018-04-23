package com.gameofhands;

import java.sql.SQLException;

import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class LoginEventHandler extends BaseServerEventHandler {

	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {

		SFSObject loginData = (SFSObject) event.getParameter(SFSEventParam.LOGIN_IN_DATA);
		String ipAddress = ((ISession) event.getParameter(SFSEventParam.SESSION)).getAddress();
		String userLoginId = (String) event.getParameter(SFSEventParam.LOGIN_NAME);
		String sessionToken = loginData.getUtfString("sessionToken");

		boolean foundToken = tryFindToken(userLoginId, sessionToken, ipAddress);

		if (!foundToken) {
			SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
			errData.addParameter("session token not valid");
			throw new SFSLoginException("Gonzo and Kermit are not allowed in this Zone!", errData);
		}

		ISFSObject user = getUser(userLoginId);

		ISFSObject outData = (ISFSObject) event.getParameter(SFSEventParam.LOGIN_OUT_DATA);
		outData.putUtfString("userDisplayName", user.getUtfString("display_name"));	

	}

	private ISFSObject getUser(String userLoginId) throws SFSLoginException {
		IDBManager dbManager = getParentExtension().getParentZone().getDBManager();
		String sql = "SELECT * FROM users WHERE user_login_id = ?";

		try {
			// Obtain a resultset
			ISFSArray res = dbManager.executeQuery(sql, new Object[] { userLoginId});
			return (ISFSObject)res.get(0).getObject();			

		} catch (SQLException e) {
			throw new SFSLoginException("Could not fetch user from database");
		}
	}

	private boolean tryFindToken(String userLoginId, String sessionToken, String ipAddress) {

		IDBManager dbManager = getParentExtension().getParentZone().getDBManager();
		String sql = "SELECT * FROM user_sessions WHERE user_login_id = ? AND session_token = ? AND ip_address = ?";
		
		try {
			// Obtain a resultset
			ISFSArray res = dbManager.executeQuery(sql, new Object[] { userLoginId, sessionToken, ipAddress });
			if (res.size() != 1) {
				return false;
			}		
			
		} catch (SQLException e) {
			return false;
		}

		return true;
	}

}
