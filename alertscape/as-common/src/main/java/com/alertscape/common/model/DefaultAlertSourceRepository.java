/**
 * 
 */
package com.alertscape.common.model;

import java.util.List;
import java.util.Map;

import com.alertscape.common.dao.AlertSourceDao;
import com.alertscape.common.dao.DaoException;

/**
 * @author josh
 * 
 */
public class DefaultAlertSourceRepository implements AlertSourceRepository {
  private static final AlertSource DEFAULT_SOURCE = new AlertSource(1, "UNKNOWN");
  private AlertSourceDao sourceDao;
  private Map<String, AlertSource> alertSources;

  public DefaultAlertSourceRepository(Map<String, AlertSource> alertSources) {
    this.alertSources = alertSources;
  }

  public AlertSource getAlertSource(String name) {
    AlertSource alertSource = alertSources == null ? null : alertSources.get(name);

    return alertSource == null ? DEFAULT_SOURCE : alertSource;
  }
  
  public void init() {
    try {
      List<AlertSource> allSources = getSourceDao().getAllSources();
      for (AlertSource alertSource : allSources) {
        alertSources.put(alertSource.getSourceName(), alertSource);
      }
    } catch (DaoException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
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
