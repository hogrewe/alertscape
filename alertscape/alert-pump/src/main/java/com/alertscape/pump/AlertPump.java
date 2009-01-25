/**
 * 
 */
package com.alertscape.pump;

import java.util.List;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertAttributeDefinition;
import com.alertscape.common.model.AlertSource;

/**
 * @author josh
 * 
 */
public interface AlertPump {
  void processAlert(Alert a) throws AlertscapeException;

  List<Alert> registerAlertSource(AlertSource source, AlertSourceCallback callback) throws AlertscapeException;

  List<Alert> getAllAlerts() throws AlertscapeException;

  AlertSource getAlertSource(String sourceName) throws AlertscapeException;

  List<AlertAttributeDefinition> getAttributeDefinitions();

  /**
   * @param a
   * @throws AlertscapeException
   */
  void processUprampAlert(Alert a) throws AlertscapeException;
}
