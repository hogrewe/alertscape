package com.alertscape.browser.model;

import java.util.List;

import com.alertscape.browser.common.auth.User;
import com.alertscape.common.model.Alert;

public class BrowserContext
{

	private List<Alert> selectedAlerts; // the alerts that are currently
										// selected by the user in their primary
										// alert panel
	private User currentUser; // the current user that is logged into the system

	public void setCurrentUser(User currentUser)
	{
		this.currentUser = currentUser;
	}

	public User getCurrentUser()
	{
		return currentUser;
	}

	public void setSelectedAlerts(List<Alert> selectedAlerts)
	{
		this.selectedAlerts = selectedAlerts;
	}

	public List<Alert> getSelectedAlerts()
	{
		return selectedAlerts;
	}

}
