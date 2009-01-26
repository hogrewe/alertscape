/**
 * 
 */
package com.alertscape.pump.onramp.db;

import java.util.List;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public interface AlertOnrampDao {
  /**
   * This method should read in at most batchSize alerts from the database, starting at lastId and append the alerts to
   * nextAlerts. The return value is the last "id" pulled from the database.
   * 
   * @param batchSize
   * @param lastId
   * @param nextAlerts
   * @return
   */
  Object getNextAlerts(int batchSize, Object lastId, List<Alert> nextAlerts);
}
