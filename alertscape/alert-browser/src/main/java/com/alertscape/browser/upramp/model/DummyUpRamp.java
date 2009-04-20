package com.alertscape.browser.upramp.model;

import java.util.HashMap;
import java.util.Map;
import com.alertscape.browser.model.BrowserContext;
import com.alertscape.browser.upramp.model.UpRamp;

public class DummyUpRamp implements UpRamp
{

	public Map initialize(BrowserContext context)
	{
		HashMap map = new HashMap();
		return map;
	}

	public boolean isAuthorized(BrowserContext context)
	{
		return true;
	}

	public boolean submit(BrowserContext context, Map keyvals)
	{
		return true;
	}

}
