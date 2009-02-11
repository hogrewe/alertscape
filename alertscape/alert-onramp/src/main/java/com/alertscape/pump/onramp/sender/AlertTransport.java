/**
 * 
 */
package com.alertscape.pump.onramp.sender;

import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.equator.AlertEquator;
import com.alertscape.pump.AlertSourceCallback;

/**
 * @author josh
 * 
 */
public interface AlertTransport {
  void sendAlert(Alert a) throws AlertTransportException;

  long registerAlertSource(AlertSource source, AlertSourceCallback callback) throws AlertTransportException;

  AlertSource getSource(String sourceName) throws AlertTransportException;

  Alert getAlert(AlertSource source, long alertId) throws AlertTransportException;

  /**
   * @param a
   * @param equator
   * @return
   */
  Alert getAlert(Alert a, AlertEquator equator);
}
