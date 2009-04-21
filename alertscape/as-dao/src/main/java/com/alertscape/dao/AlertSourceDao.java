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
  AlertSource get(int sourceId);

  List<AlertSource> getAllSources();

  void save(AlertSource source);

  void updateAlertIdSeq(final AlertSource source, final long alertId);

  long getAlertIdSeq(AlertSource source);
}
