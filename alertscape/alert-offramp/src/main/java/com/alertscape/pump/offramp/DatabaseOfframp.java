/**
 * 
 */
package com.alertscape.pump.offramp;

import java.util.List;

import com.alertscape.AlertscapeException;
import com.alertscape.common.dao.AlertDao;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;

/**
 * @author josh
 * 
 */
public class DatabaseOfframp implements AlertOfframp {
  private AlertDao alertDao;

  public void processAlert(Alert alert) throws AlertscapeException {
    if (alert.isStanding()) {
      getAlertDao().save(alert);
    } else {
      // Might we want to just update with a cleared status then have an alert reaper?
      getAlertDao().delete(alert.getAlertId());
    }
  }

  public List<Alert> getAlertsForSource(AlertSource source) throws AlertscapeException {
    return getAlertDao().getAlertsForSource(source);
  }

  /**
   * @return the alertDao
   */
  public AlertDao getAlertDao() {
    return alertDao;
  }

  /**
   * @param alertDao
   *          the alertDao to set
   */
  public void setAlertDao(AlertDao alertDao) {
    this.alertDao = alertDao;
  }
}
