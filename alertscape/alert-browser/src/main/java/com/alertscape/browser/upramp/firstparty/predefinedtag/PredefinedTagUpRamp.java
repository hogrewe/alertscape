package com.alertscape.browser.upramp.firstparty.predefinedtag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alertscape.browser.model.BrowserContext;
import com.alertscape.browser.ui.swing.AlertBrowser;
import com.alertscape.browser.upramp.model.UpRamp;
import com.alertscape.common.model.Alert;

public class PredefinedTagUpRamp implements UpRamp
{

	public Map initialize(BrowserContext context)
	{
		Map myMap = AlertBrowser.getCategories();
		
		return myMap;
	}

	public boolean isAuthorized(BrowserContext context)
	{
		// TODO Auto-generated method stub
		return true;
	}

	public boolean submit(BrowserContext context, Map keyvals)
	{
		List<Alert> alerts = context.getSelectedAlerts();
		Iterator<Alert> it = alerts.iterator();
		
		String tagName = (String)keyvals.get(PredefinedTagConstants.TAG_NAME);
		String tagValue = (String)keyvals.get(PredefinedTagConstants.TAG_VALUE);
			
    AlertBrowser.categorize(alerts, tagName, tagValue);
    return true;
	}
}
