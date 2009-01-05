package com.alertscape.browser.model;

import java.util.List;

import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AuthenticatedUser;

public class BrowserContext
{

	private List<Alert> selectedAlerts; // the alerts that are currently
										// selected by the user in their primary
										// alert panel
	private AuthenticatedUser currentUser; // the current user that is logged into the system

	public void setCurrentUser(AuthenticatedUser currentUser)
	{
		this.currentUser = currentUser;
	}

	public AuthenticatedUser getCurrentUser()
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
