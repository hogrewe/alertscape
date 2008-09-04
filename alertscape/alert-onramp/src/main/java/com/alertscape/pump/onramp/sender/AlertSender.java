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
public interface AlertSender {
  void sendAlert(Alert a) throws AlertSendingException;
  List<Alert> getAlerts(AlertSource source) throws AlertSendingException;
}
