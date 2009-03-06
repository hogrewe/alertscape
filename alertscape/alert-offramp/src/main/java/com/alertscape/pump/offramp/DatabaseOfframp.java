/**
 * 
 */
package com.alertscape.pump.offramp;

import java.util.List;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.equator.AlertEquator;
import com.alertscape.dao.AlertDao;
import com.alertscape.dao.DaoException;

/**
 * @author josh
 * 
 */
public class DatabaseOfframp implements AlertOfframp {
  private AlertDao alertDao;

  // public void processQueuedAlert(Alert alert) throws AlertscapeException {
  public void processAlert(Alert alert) throws AlertscapeException {
    if (alert.isStanding()) {
      getAlertDao().save(alert);
    } else {
      // Might we want to just update with a cleared status then have an alert reaper?
      getAlertDao().delete(alert.getSource(), alert.getAlertId());
    }
  }

  public List<Alert> getAlertsForSource(AlertSource source) throws AlertscapeException {
    return getAlertDao().getAlertsForSource(source);
  }

  public List<Alert> getAllAlerts(String filter) throws AlertscapeException {
    return getAlertDao().getAllAlerts(filter);
  }

  /**
   * @param source
   * @param alertId
   * @return
   * @throws DaoException
   */
  public Alert getAlert(AlertSource source, long alertId) throws AlertscapeException {
    return getAlertDao().get(source, alertId);
  }
  
  public Alert getAlert(Alert a, AlertEquator equator) {
    return getAlertDao().get(a, equator);
  }

  public String getOfframpName() {
    return "DBOfframp";
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
