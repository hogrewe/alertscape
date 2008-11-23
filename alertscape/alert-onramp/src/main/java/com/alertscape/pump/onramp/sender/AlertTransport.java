/**
 * 
 */
package com.alertscape.pump.onramp.sender;

import java.util.List;

import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;

/**
 * @author josh
 *
 */
public interface AlertTransport {
  void sendAlert(Alert a) throws AlertTransportException;
  List<Alert> getAlerts(AlertSource source) throws AlertTransportException;
}
