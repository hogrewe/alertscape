/**
 * 
 */
package com.alertscape.dao;

import java.util.List;

import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.equator.AlertEquator;

/**
 * @author josh
 *
 */
public interface AlertDao {
	public void save(Alert alert) throws DaoException;
	public Alert get(AlertSource source, long alertId) throws DaoException;
	public void delete(AlertSource source, long alertId) throws DaoException;
	public List<Alert> getAllAlerts() throws DaoException;
	public List<Alert> getAlertsForSource(AlertSource source) throws DaoException;
  /**
   * @param a
   * @param equator
   * @return
   */
  Alert get(Alert a, AlertEquator equator);
}
