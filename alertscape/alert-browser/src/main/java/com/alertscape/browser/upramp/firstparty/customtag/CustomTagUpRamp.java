package com.alertscape.browser.upramp.firstparty.customtag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alertscape.browser.model.BrowserContext;
import com.alertscape.browser.upramp.model.UpRamp;

public class CustomTagUpRamp implements UpRamp
{

	public Map initialize(BrowserContext context)
	{
		// TODO Auto-generated method stub
		Map myMap = new HashMap();
		List fakeTagList = new ArrayList();
		fakeTagList.add("Location");
		fakeTagList.add("Known Event Name");
		fakeTagList.add("Ticket Number");
		fakeTagList.add("Workflow Status");
		myMap.put(CustomTagConstants.EXISTING_TAGNAMES, fakeTagList);
		return myMap;
	}

	public boolean isAuthorized(BrowserContext context)
	{
		// TODO Auto-generated method stub
		return true;
	}

	public boolean submit(BrowserContext context, Map keyvals)
	{
		// TODO Auto-generated method stub
		return true;
	}

}
