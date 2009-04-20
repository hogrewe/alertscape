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

/**
 * @author josh
 * 
 */
public class ConfigurableAlertPump implements AlertPump {
  private DatabaseOfframp dbOfframp;
  private JmsOfframp jmsOfframp;
  private AlertSourceRepository alertSourceRepository;
  private AlertAttributeDefinitionDao definitionDao;
  private Map<AlertSource, AlertSourceCallback> callbacks = new HashMap<AlertSource, AlertSourceCallback>();

  public void processAlert(Alert a) throws AlertscapeException {
    if (dbOfframp != null) {
      dbOfframp.processAlert(a);
    }
    if (jmsOfframp != null) {
      jmsOfframp.processAlert(a);
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
  public void setDbOfframp(DatabaseOfframp dbOfframp) {
    this.dbOfframp = dbOfframp;
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
