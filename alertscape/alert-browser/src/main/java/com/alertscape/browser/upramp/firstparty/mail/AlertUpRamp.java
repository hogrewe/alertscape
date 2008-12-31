package com.alertscape.browser.upramp.firstparty.mail;

import java.util.HashMap;
import java.util.Map;
import com.alertscape.browser.model.BrowserContext;
import com.alertscape.browser.upramp.firstparty.mail.AlertMailConstants.BodyMode;
import com.alertscape.browser.upramp.firstparty.mail.AlertMailConstants.SubjectMode;
import com.alertscape.browser.upramp.model.UpRamp;

public class AlertUpRamp implements UpRamp
{

	public Map initialize(BrowserContext context)
	{
		// TODO make this actually work (call the server to get this info), the below is just for demo purposes
		HashMap map = new HashMap();
		map.put(AlertMailConstants.DEFAULT_TO_EMAILS, "tier3@alertscape.com");
		map.put(AlertMailConstants.DEFAULT_CC_EMAILS, "DL_ops_mgt@alertscape.com");
		map.put(AlertMailConstants.SUBJECT_MODE, SubjectMode.LONG_DESC);
		map.put(AlertMailConstants.BODY_MODE, BodyMode.KEY_VALS);
		
		return map;
	}

	public boolean isAuthorized(BrowserContext context)
	{
		// TODO Auto-generated method stub
		return true;
	}

	public boolean submit(BrowserContext context, Map keyvals)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
