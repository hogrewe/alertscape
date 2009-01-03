package com.alertscape.browser.upramp.firstparty.login;

import java.util.HashMap;
import java.util.Map;

import com.alertscape.browser.common.auth.Authentication;
import com.alertscape.browser.common.auth.User;
import com.alertscape.browser.model.BrowserContext;
import com.alertscape.browser.ui.swing.AlertBrowser;
import com.alertscape.browser.upramp.model.UpRamp;

public class LoginUpRamp implements UpRamp
{

	public Map initialize(BrowserContext context)
	{
  	// TODO make this actually work (call the server to get this info), the below is just for demo purposes
		// not sure at this point when/why we would need to initialize - back to the most recent login, from disk? don't know
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
		User user = Authentication.login("CEV", username, null);    
    AlertBrowser.getCurrentContext().setCurrentUser(user);
		
		return true;
	}

}
