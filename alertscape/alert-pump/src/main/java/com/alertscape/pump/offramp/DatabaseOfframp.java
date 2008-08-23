/**
 * 
 */
package com.alertscape.pump.offramp;

import com.alertscape.AlertscapeException;
import com.alertscape.common.dao.AlertDao;
import com.alertscape.common.model.Alert;

/**
 * @author josh
 *
 */
public class DatabaseOfframp implements AlertOfframp {
	private AlertDao alertDao;

	public void processAlert(Alert alert) throws AlertscapeException {
		getAlertDao().save(alert);
	}

	/**
	 * @return the alertDao
	 */
	public AlertDao getAlertDao() {
		return alertDao;
	}

	/**
	 * @param alertDao the alertDao to set
	 */
	public void setAlertDao(AlertDao alertDao) {
		this.alertDao = alertDao;
	}
}
