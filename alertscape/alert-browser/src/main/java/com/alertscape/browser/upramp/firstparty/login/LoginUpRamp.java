package com.alertscape.browser.upramp.firstparty.login;

import java.util.HashMap;
import java.util.Map;

import com.alertscape.browser.model.BrowserContext;
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
		// TODO Auto-generated method stub - make this work
		return false;
	}

}
