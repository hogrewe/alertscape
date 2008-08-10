/**
 * 
 */
package com.alertscape.common.dao;

import java.util.List;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 *
 */
public interface AlertDao {
	public void save(Alert alert) throws DaoException;
	public Alert get(long alertId) throws DaoException;
	public Alert delete(long alertId) throws DaoException;
	public List<Alert> getAllAlerts() throws DaoException;
}
