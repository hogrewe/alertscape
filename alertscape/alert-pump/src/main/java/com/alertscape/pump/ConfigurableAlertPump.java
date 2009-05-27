/**
 * 
 */
package com.alertscape.pump;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertAttributeDefinition;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.AlertSourceRepository;
import com.alertscape.common.model.equator.AlertEquator;
import com.alertscape.dao.AlertAttributeDefinitionDao;
import com.alertscape.pump.offramp.DatabaseOfframp;
import com.alertscape.pump.offramp.JmsOfframp;
import com.alertscape.pump.offramp.MemoryOfframp;

/**
 * @author josh
 * 
 */
public class ConfigurableAlertPump implements AlertPump {
  private DatabaseOfframp dbOfframp;
  private JmsOfframp jmsOfframp;
  private MemoryOfframp memoryOfframp;
  private AlertSourceRepository alertSourceRepository;
  private AlertAttributeDefinitionDao definitionDao;
  private Map<AlertSource, AlertSourceCallback> callbacks = new HashMap<AlertSource, AlertSourceCallback>();

  public void processAlert(Alert a) throws AlertscapeException {
  	// enrich the alert with attrs that did not come from the onramp (because this uses the memory offramp to get the previous instance of the alert, must do this prior to the alert update.
  	// also, this must be done prior to the alert being sent to the client, or you will have the effect of the alerts popping out of categories.
  	enrichAlertInternal(a);

  	if (dbOfframp != null) {
      dbOfframp.processAlert(a);
    }
  	
  	// third, update the memory offramp with this latest data
  	if (memoryOfframp != null) {
      memoryOfframp.processAlert(a);
    }
  	
    // last, update the ui's with this latest data
    if (jmsOfframp != null) {
      jmsOfframp.processAlert(a);
    }
  }

  // this method will look up internal (categories, labels, etc) attributes associated with the alert and put them into the alert prior to dumping it to an offramp
  public void enrichAlertInternal(Alert a) throws AlertscapeException 
  {
  	// try to grab a previous version of this alert
  	Alert prev = memoryOfframp.getAlert(a.getAlertId());
  	
  	// check if a previous version was found
  	if (prev != null)
  	{
  		// if one was found, enrich the alert with the previous major and minor tags
  		if (a.getMajorTags() == null || a.getMajorTags().keySet().size() == 0)
  		{
  			a.setMajorTags(prev.getMajorTags());
  			a.setMinorTags(prev.getMinorTags());
  		}
  	}
  }
  
  public void processUprampAlert(Alert a) throws AlertscapeException {
    processAlert(a);
    AlertSourceCallback callback = callbacks.get(a.getSource());
    if (callback != null) {
      callback.updateAlert(a);
    }
  }

  public long registerAlertSource(AlertSource source, AlertSourceCallback callback) throws AlertscapeException {
    if (callback != null) {
      callbacks.put(source, callback);
    }
    return getAlertSourceRepository().getNextAlertIdSeq(source);
  }

  public Alert getAlert(AlertSource source, long alertId) throws AlertscapeException {
    return getDbOfframp().getAlert(source, alertId);
  }

  public Alert getAlert(Alert a, AlertEquator equator) {
    return getDbOfframp().getAlert(a, equator);
  }

  public List<Alert> findMatching(Alert a, AlertEquator equator) {
    return getDbOfframp().findMatching(a, equator);
  }

  public List<Alert> getAllAlerts(String filter) throws AlertscapeException {
    return getDbOfframp().getAllAlerts(filter);
  }

  public AlertSource getAlertSource(String name) throws AlertscapeException {
    return getAlertSourceRepository().getAlertSource(name);
  }

  public List<AlertAttributeDefinition> getAttributeDefinitions() {
    return getDefinitionDao().getActiveDefinitions();
  }

  public Map<String, Object> getCategoryDefinitions() {
    return getDefinitionDao().getActiveCategoryDefinitions();
  }
  
  public Map<String, List<String>> getLabelDefinitions() {
    return getDefinitionDao().getActiveLabelDefinitions();
  }
  
  /**
   * @return the dbOfframp
   */
  public DatabaseOfframp getDbOfframp() {
    return dbOfframp;
  }

  /**
   * @param dbOfframp
   *          the dbOfframp to set
   */
  public void setDbOfframp(DatabaseOfframp dbOfframp) 
  {
    this.dbOfframp = dbOfframp;
    
    // TODO: this is completely ridiculous, but I don't know where else to force triggering creating a memory offramp when a dbofframp is created ==> Ask Josh later       
    try
		{
			memoryOfframp = new MemoryOfframp(dbOfframp.getAllAlerts(""));
		} catch (AlertscapeException e)
		{
			e.printStackTrace();
		}
  }

  /**
   * @return the jmsOfframp
   */
  public JmsOfframp getJmsOfframp() {
    return jmsOfframp;
  }

  /**
   * @param jmsOfframp
   *          the jmsOfframp to set
   */
  public void setJmsOfframp(JmsOfframp jmsOfframp) {
    this.jmsOfframp = jmsOfframp;
  }

  /**
   * @return the alertSourceRepository
   */
  public AlertSourceRepository getAlertSourceRepository() {
    return alertSourceRepository;
  }

  /**
   * @param alertSourceRepository
   *          the alertSourceRepository to set
   */
  public void setAlertSourceRepository(AlertSourceRepository alertSourceRepository) {
    this.alertSourceRepository = alertSourceRepository;
  }

  /**
   * @return the definitionDao
   */
  public AlertAttributeDefinitionDao getDefinitionDao() {
    return definitionDao;
  }

  /**
   * @param definitionDao
   *          the definitionDao to set
   */
  public void setDefinitionDao(AlertAttributeDefinitionDao definitionDao) {
    this.definitionDao = definitionDao;
  }

}
