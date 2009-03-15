package com.alertscape.browser.upramp.firstparty.customtag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alertscape.browser.model.BrowserContext;
import com.alertscape.browser.ui.swing.AlertBrowser;
import com.alertscape.browser.upramp.model.UpRamp;
import com.alertscape.common.model.Alert;

public class CustomTagUpRamp implements UpRamp
{

	public Map initialize(BrowserContext context)
	{	
		return AlertBrowser.getLabels();
	}

	public boolean isAuthorized(BrowserContext context)
	{
		// TODO Auto-generated method stub
		return true;
	}

	public boolean submit(BrowserContext context, Map keyvals)
	{
		// TODO Auto-generated method stub - this is just hacked for now, it adds the tags locally and returns,
		// it needs to send the alerts and data up to the server, and have this same thing happen on the server, like an onramp
		// so that all clients get the same change, not just this one
		List<Alert> alerts = context.getSelectedAlerts();
		Iterator<Alert> it = alerts.iterator();
		
		String tagName = (String)keyvals.get(CustomTagConstants.TAG_NAME);
		String tagValue = (String)keyvals.get(CustomTagConstants.TAG_VALUE);
		
    AlertBrowser.label(alerts, tagName, tagValue);
		
		return true;
	}

}
