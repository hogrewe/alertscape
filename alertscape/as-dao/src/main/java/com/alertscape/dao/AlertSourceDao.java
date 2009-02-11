/**
 * 
 */
package com.alertscape.dao;

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
  void updateAlertIdSeq(final AlertSource source, final long alertId);
  long getAlertIdSeq(AlertSource source);
}
