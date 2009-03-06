/**
 * 
 */
package com.alertscape.pump;

import java.util.List;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertAttributeDefinition;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.equator.AlertEquator;

/**
 * @author josh
 * 
 */
public interface AlertPump {
  void processAlert(Alert a) throws AlertscapeException;

  long registerAlertSource(AlertSource source, AlertSourceCallback callback) throws AlertscapeException;

  List<Alert> getAllAlerts(String filter) throws AlertscapeException;

  AlertSource getAlertSource(String sourceName) throws AlertscapeException;

  List<AlertAttributeDefinition> getAttributeDefinitions();
  
  Alert getAlert(AlertSource source, long alertId) throws AlertscapeException;

  /**
   * @param a
   * @throws AlertscapeException
   */
  void processUprampAlert(Alert a) throws AlertscapeException;

  /**
   * @param a
   * @param equator
   * @return
   */
  Alert getAlert(Alert a, AlertEquator equator);
}
