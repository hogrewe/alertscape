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
  AlertSource get(int sourceId) throws DaoException;
  List<AlertSource> getAllSources() throws DaoException;
  void save(AlertSource source) throws DaoException;
}
