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
  AlertSource getSource(int sourceId) throws AlertTransportException;
  AlertSource getSource(String sourceName) throws AlertTransportException;
  long getNextAlertId(int skipCount) throws AlertTransportException;
}
