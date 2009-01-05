package com.alertscape.browser.upramp.firstparty.login;

import java.util.HashMap;
import java.util.Map;

import com.alertscape.browser.common.auth.Authentication;
import com.alertscape.browser.model.BrowserContext;
import com.alertscape.browser.ui.swing.AlertBrowser;
import com.alertscape.browser.upramp.model.UpRamp;
import com.alertscape.common.model.AuthenticatedUser;

public class LoginUpRamp implements UpRamp
{

	public Map initialize(BrowserContext context)
	{
  	// TODO make this actually work (call the server to get this info), the below is just for demo purposes
		// not sure at this point when/why we would need to initialize - back to the most recent login, from disk? don't know
		// actually, one thing that might come down from the server in the map is the method of authentication: use LDAP, or basic authentication via alertscape, or?
		// and then this would drive how the submit method works....
		HashMap map = new HashMap();
		return map;
	}

	public boolean isAuthorized(BrowserContext context)
	{
		// TODO Auto-generated method stub - make this work
		return true;
	}

	public boolean submit(BrowserContext context, Map keyvals)
	{
		// TODO Auto-generated method stub - make this work for real, this is just a demo hack below
		String username = (String)keyvals.get(LoginConstants.USER_ID);
		char[] password = (char[])keyvals.get(LoginConstants.PASSWORD);
		AuthenticatedUser user = Authentication.login("CEV", username, password);    
    AlertBrowser.getCurrentContext().setCurrentUser(user);
		
		return true;
	}

}
