/**
 * 
 */
package com.alertscape.common.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.dao.AlertSourceDao;
import com.alertscape.dao.DaoException;

/**
 * @author josh
 * 
 */
public class DefaultAlertSourceRepository implements AlertSourceRepository {
  private static final ASLogger LOG = ASLogger.getLogger(DefaultAlertSourceRepository.class);
  private static final AlertSource DEFAULT_SOURCE = new AlertSource(1, "UNKNOWN");
  private AlertSourceDao sourceDao;
  private Map<String, AlertSource> alertSources = new HashMap<String, AlertSource>();
  private Map<Integer, AlertSource> idToSource = new HashMap<Integer, AlertSource>();

  public DefaultAlertSourceRepository() {
    
  }
  
  public DefaultAlertSourceRepository(Map<String, AlertSource> alertSources) {
    this.alertSources = alertSources;
  }

  public AlertSource getAlertSource(String name) {
    AlertSource alertSource = alertSources == null ? null : alertSources.get(name);

    return alertSource == null ? DEFAULT_SOURCE : alertSource;
  }

  public AlertSource getAlertSource(int sourceId) {
    AlertSource alertSource = idToSource.get(sourceId);

    return alertSource == null ? DEFAULT_SOURCE : alertSource;
  }
  
  public void updateAlertIdSeq(AlertSource source, long alertId) {
    getSourceDao().updateAlertIdSeq(source, alertId);
  }
  
  public long getNextAlertIdSeq(AlertSource source) {
    return getSourceDao().getAlertIdSeq(source);
  }

  public void init() {
    try {
      List<AlertSource> allSources = getSourceDao().getAllSources();
      for (AlertSource alertSource : allSources) {
        alertSources.put(alertSource.getSourceName(), alertSource);
        idToSource.put(alertSource.getSourceId(), alertSource);
      }
    } catch (DaoException e) {
      LOG.error("Couldn't get sources from the DB", e);
    }
  }

  /**
   * @return the sourceDao
   */
  public AlertSourceDao getSourceDao() {
    return sourceDao;
  }

  /**
   * @param sourceDao the sourceDao to set
   */
  public void setSourceDao(AlertSourceDao sourceDao) {
    this.sourceDao = sourceDao;
  }
}
