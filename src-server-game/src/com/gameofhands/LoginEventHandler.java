package com.gameofhands;

import java.sql.SQLException;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.entities.data.ISFSArray;
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
		
		String userLoginId = (String) event.getParameter(SFSEventParam.LOGIN_NAME);		
		String sessionToken = loginData.getUtfString("sessionToken");
		
		boolean foundToken = tryFindToken(userLoginId, sessionToken);
		
		if (!foundToken) {			
			SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
			errData.addParameter("session token not valid");			
			throw new SFSLoginException("Gonzo and Kermit are not allowed in this Zone!", errData);
		}

	}

	private boolean tryFindToken(String userLoginId, String sessionToken) {
		
		IDBManager dbManager = getParentExtension().getParentZone().getDBManager();
        String sql = "SELECT * FROM user_sessions WHERE user_login_id = ? AND session_token = ?";
          
        try
        {
            // Obtain a resultset
            ISFSArray res = dbManager.executeQuery(sql, new Object[] {userLoginId, sessionToken});
            if (res.size() != 1) {
				return false;
			}
            
        }
        catch (SQLException e)
        {
            return false;
        }
        
        return true;
	}

}
