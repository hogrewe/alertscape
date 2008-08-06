/**
 * 
 */
package com.alertscape.common.dao;

import java.util.List;

import com.alertscape.common.model.Event;

/**
 * @author josh
 *
 */
public interface AlertDao {
	public void save(Event alert) throws DaoException;
	public Event get(long alertId) throws DaoException;
	public Event delete(long alertId) throws DaoException;
	public List<Event> getAllAlerts() throws DaoException;
}
