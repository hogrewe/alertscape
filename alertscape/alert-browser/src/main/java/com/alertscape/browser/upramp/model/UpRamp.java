package com.alertscape.browser.upramp.model;

import java.util.Map;

import com.alertscape.browser.model.BrowserContext;

public interface UpRamp 
{
	// this interface will be executed in the following order:
	//   isAuthorized (if true, continue)
	//	 initialize
	//	 submit
	
	// ability to check if this user, with this context, is authorized to do this operation
	public boolean isAuthorized(BrowserContext context);
	
	// ability to initialize a ui component based on the given browser context 
	public Map initialize(BrowserContext context);
	
	// ability to submit information back to the server, from the ui panel
	public boolean submit(BrowserContext context, Map keyvals);
}
