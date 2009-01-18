package com.alertscape.browser.localramp.firstparty.preferences;

import java.util.Map;

public interface UserPreferencesPanel
{
	/*
	 * Set the preferences for the given panel
	 */
	public void setUserPreferences(Map preferences);
	
	/*
	 * Get the preferences for the given panel
	 */
	public Map getUserPreferences();
}
