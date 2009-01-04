/**
 * 
 */
package com.alertscape.pump;

import java.util.List;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;

/**
 * @author josh
 * 
 */
public interface AlertPump {
  void processAlert(Alert a) throws AlertscapeException;

  List<Alert> getAlerts(AlertSource source) throws AlertscapeException;

  List<Alert> getAllAlerts() throws AlertscapeException;

  AlertSource getAlertSource(String sourceName) throws AlertscapeException;
}
