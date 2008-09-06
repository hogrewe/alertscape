/**
 * 
 */
package com.alertscape.common.dao;

import java.util.List;

import com.alertscape.common.model.AlertSource;

/**
 * @author josh
 *
 */
public interface AlertSourceDao {
  public AlertSource get(int sourceId) throws DaoException;
  public List<AlertSource> getAllSources() throws DaoException;
}
