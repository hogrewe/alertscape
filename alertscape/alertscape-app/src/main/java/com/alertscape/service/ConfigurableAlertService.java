/**
 * 
 */
package com.alertscape.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alertscape.AlertscapeException;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertAttributeDefinition;
import com.alertscape.common.model.AlertStatus;
import com.alertscape.common.model.AuthenticatedUser;
import com.alertscape.dao.TreeConfigurationDao;
import com.alertscape.pump.AlertPump;
import com.alertscape.service.tags.CustomTagConstants;
import com.alertscape.service.tags.PredefinedTagConstants;
import com.alertscape.service.tags.PredefinedTagProfile;

/**
 * @author josh
 * 
 */
public class ConfigurableAlertService implements AlertService {
  private static final ASLogger LOG = ASLogger.getLogger(ConfigurableAlertService.class);
  private AlertPump pump;
  private TreeConfigurationDao treeConfigurationDao;

  public List<Alert> getAllAlerts(String filter) throws AlertscapeException {
    return pump.getAllAlerts(filter);
  }

  public void acknowledge(AuthenticatedUser user, List<Alert> alert) throws AlertscapeException {
    for (Alert a : alert) {
      a.setAcknowledgedBy(user.getUsername());
      LOG.info("Acknowledging alert: " + a.getCompositeAlertId());
      pump.processUprampAlert(a);
    }
  }

  public void unacknowledge(AuthenticatedUser user, List<Alert> alert) throws AlertscapeException {
    for (Alert a : alert) {
      a.setAcknowledgedBy(null);
      LOG.info("Unacknowledging alert: " + a.getCompositeAlertId());
      pump.processUprampAlert(a);
    }
  }

  public void clear(AuthenticatedUser user, List<Alert> alert) throws AlertscapeException {
    for (Alert a : alert) {
      a.setStatus(AlertStatus.CLEARED);
      LOG.info("Clearing alert: " + a.getCompositeAlertId());
      pump.processUprampAlert(a);
    }
  }

  public List<AlertAttributeDefinition> getAttributeDefinitions() throws AlertscapeException {
    return pump.getAttributeDefinitions();
  }

  @Override
  public String getTreeConfiguration() throws AlertscapeException {
    return getTreeConfigurationDao().getTreeConfiguration();
  }

  /**
   * @return the pump
   */
  public AlertPump getPump() {
    return pump;
  }

  /**
   * @param pump
   *          the pump to set
   */
  public void setPump(AlertPump pump) {
    this.pump = pump;
  }

  /**
   * @return the treeConfigurationDao
   */
  public TreeConfigurationDao getTreeConfigurationDao() {
    return treeConfigurationDao;
  }

  /**
   * @param treeConfigurationDao the treeConfigurationDao to set
   */
  public void setTreeConfigurationDao(TreeConfigurationDao treeConfigurationDao) {
    this.treeConfigurationDao = treeConfigurationDao;
  }
  
	@Override
	public void categorize(AuthenticatedUser user, List<Alert> alerts, String tagName, String tagValue) throws AlertscapeException
	{
    for (Alert a : alerts) 
    {
	      a.addMajorTag(tagName, tagValue);	      
	      LOG.info("categorizing alert: " + a.getCompositeAlertId());
	      pump.processUprampAlert(a);
	  }
	}
	
	@Override
	public void label(AuthenticatedUser user, List<Alert> alerts, String tagName, String tagValue) throws AlertscapeException
	{
		for (Alert a : alerts) 
    {
	      a.addMinorTag(tagName, tagValue);	      
	      LOG.info("labeling alert: " + a.getCompositeAlertId());
	      pump.processUprampAlert(a);
	  }		
	}

	@Override
	public Map<String, ?> getCategories(AuthenticatedUser user) throws AlertscapeException
	{
		LOG.info("getting category definitions");
		
		// TODO - these are all hardcoded, they need to come from a file or a database instead
		// remember that major tags / categories need to be predefined, and cannot have custom names, but they can have custom values
		// may want to select * from the predefined values table, and join it with all of the values for the given tags in the database
		// alphabetize, and provde the results back....
		Map<String, Object> myMap = new HashMap<String, Object>();
		List<String> fakeTagList = new ArrayList<String>();
		fakeTagList.add("Status");
		fakeTagList.add("Folder");
		fakeTagList.add("Responsible Dept");
		myMap.put(PredefinedTagConstants.DEFINED_TAGNAMES, fakeTagList);
		
		List<String> validStatuses = new ArrayList<String>();
		validStatuses.add("Assigned");
		validStatuses.add("Work in Progress");
		validStatuses.add("Waiting for Customer");
		validStatuses.add("Resolved");
		PredefinedTagProfile statusProfile = new PredefinedTagProfile();
		statusProfile.setValidValues(validStatuses);
		statusProfile.setUserModifiable(false);
		statusProfile.setDefaultValue("Assigned");
		myMap.put("Status" + PredefinedTagConstants.TAGPROFILE_SUFFIX, statusProfile);
	
		List<String> validFolders = new ArrayList<String>();
		validFolders.add("(None)");
		validFolders.add("Resolved");
		validFolders.add("Known Issue");
		validFolders.add("Major Event");
		PredefinedTagProfile folderProfile = new PredefinedTagProfile();
		folderProfile.setValidValues(validFolders);
		folderProfile.setUserModifiable(true);
		folderProfile.setDefaultValue("(None)");
		myMap.put("Folder" + PredefinedTagConstants.TAGPROFILE_SUFFIX, folderProfile);
	
		List<String> validDepts = new ArrayList<String>();		
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

	@Override
	public Map<String, List<String>> getLabels(AuthenticatedUser user) throws AlertscapeException
	{
		LOG.info("getting label definitions");

		// TODO - these are all hardcoded, they need to come from a file or a database instead
		// remember that minor tags / labels do NOT need to be predefined, and so they CAN have custom names, AND they can have custom values
		// may want to select * from the predefined values table, and join it with all of the names/values for the in the database
		// alphabetize, and provde the results back....
		Map<String, List<String>> myMap = new HashMap<String, List<String>>();
		List<String> fakeTagList = new ArrayList<String>();
		fakeTagList.add("Location");
		fakeTagList.add("Known Event Name");
		fakeTagList.add("Ticket Number");
		fakeTagList.add("Workflow Status");
		myMap.put(CustomTagConstants.EXISTING_TAGNAMES, fakeTagList);
		
		return myMap;
	}
}
