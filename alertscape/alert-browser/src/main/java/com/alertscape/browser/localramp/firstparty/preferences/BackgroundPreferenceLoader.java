package com.alertscape.browser.localramp.firstparty.preferences;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alertscape.browser.localramp.model.LocalRamp;
import com.alertscape.browser.localramp.model.ReadSingleObjectFromFileLocalRamp;
import com.alertscape.browser.ui.swing.AlertBrowser;
import com.alertscape.common.logging.ASLogger;

public class BackgroundPreferenceLoader
{
	private List <UserPreferencesPanel>prefPanels;
	
  public BackgroundPreferenceLoader(List <UserPreferencesPanel>prefPanels) 
  {
  	this.prefPanels = prefPanels;
  }
  
  public void loadDefaultPreferences()
  {
		try
		{
			LocalRamp ramp = new ReadSingleObjectFromFileLocalRamp("amp_default_preferences.obj");
			Map prefValues = (Map) ramp.initialize(AlertBrowser.getCurrentContext());
			if (prefValues != null)
			{
				Iterator<UserPreferencesPanel> it = prefPanels.iterator();
				while (it.hasNext())
				{
					UserPreferencesPanel panel = it.next();
					panel.setUserPreferences(prefValues);
				}
			} 
			else
			{
				// log a message that no default preferences could be found
				ASLogger.getLogger(AlertBrowser.class).debug("Unable to load default preferences");
			}
		} 
		catch (Throwable t)
		{
			// eat any exceptions: but log them
			ASLogger.getLogger(AlertBrowser.class).error("Unable to load default preferences", t);
		}  	
  }
}
