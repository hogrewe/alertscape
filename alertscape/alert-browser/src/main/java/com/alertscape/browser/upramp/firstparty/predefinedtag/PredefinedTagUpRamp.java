package com.alertscape.browser.upramp.firstparty.predefinedtag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alertscape.browser.model.BrowserContext;
import com.alertscape.browser.upramp.model.UpRamp;
import com.alertscape.common.model.Alert;

public class PredefinedTagUpRamp implements UpRamp
{

	public Map initialize(BrowserContext context)
	{
		// TODO Auto-generated method stub
		Map myMap = new HashMap();
		List fakeTagList = new ArrayList();
		fakeTagList.add("Status");
		fakeTagList.add("Folder");
		fakeTagList.add("Responsible Dept");
		myMap.put(PredefinedTagConstants.DEFINED_TAGNAMES, fakeTagList);
		
		List<String> validStatuses = new ArrayList();
		validStatuses.add("Assigned");
		validStatuses.add("Work in Progress");
		validStatuses.add("Waiting for Customer");
		validStatuses.add("Resolved");
		PredefinedTagProfile statusProfile = new PredefinedTagProfile();
		statusProfile.setValidValues(validStatuses);
		statusProfile.setUserModifiable(false);
		statusProfile.setDefaultValue("Assigned");
		myMap.put("Status" + PredefinedTagConstants.TAGPROFILE_SUFFIX, statusProfile);

		List<String> validFolders = new ArrayList();
		validFolders.add("(None)");
		validFolders.add("Resolved");
		validFolders.add("Known Issue");
		validFolders.add("Major Event");
		PredefinedTagProfile folderProfile = new PredefinedTagProfile();
		folderProfile.setValidValues(validFolders);
		folderProfile.setUserModifiable(true);
		folderProfile.setDefaultValue("(None)");
		myMap.put("Folder" + PredefinedTagConstants.TAGPROFILE_SUFFIX, folderProfile);

		List<String> validDepts = new ArrayList();		
		validDepts.add("Tier 1 NOC");
		validDepts.add("Tier 2 Customer Care");
		validDepts.add("Tier 3 Engineering");
		PredefinedTagProfile deptProfile = new PredefinedTagProfile();
		deptProfile.setValidValues(validDepts);
		deptProfile.setUserModifiable(false);
		deptProfile.setDefaultValue("Tier 1 NOC");
		myMap.put("Responsible Dept" + PredefinedTagConstants.TAGPROFILE_SUFFIX, deptProfile);		
		
		return myMap;
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
		
		String tagName = (String)keyvals.get(PredefinedTagConstants.TAG_NAME);
		String tagValue = (String)keyvals.get(PredefinedTagConstants.TAG_VALUE);
		
		while (it.hasNext())
		{
			Alert alert = it.next();
			alert.addMajorTag(tagName, tagValue);
		}
		
		return true;
	}
}
