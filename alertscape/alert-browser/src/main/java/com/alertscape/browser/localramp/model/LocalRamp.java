package com.alertscape.browser.localramp.model;

import java.util.Map;

import com.alertscape.browser.model.BrowserContext;

public interface LocalRamp
{
	// ability to initialize a ui component based on the given browser context 
	public Object initialize(BrowserContext context);
	
	// ability to submit information to the local machine based on the context
	public boolean submit(BrowserContext context, Object val);
}
