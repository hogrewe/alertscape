/**
 * 
 */
package com.alertscape.service;

import java.util.List;

import com.alertscape.AlertscapeException;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertAttributeDefinition;
import com.alertscape.common.model.AuthenticatedUser;
import com.alertscape.common.model.Alert.AlertStatus;
import com.alertscape.pump.AlertPump;

/**
 * @author josh
 * 
 */
public class ConfigurableAlertService implements AlertService {
  private static final ASLogger LOG = ASLogger.getLogger(ConfigurableAlertService.class);
  public AlertPump pump;

  public List<Alert> getAllAlerts() throws AlertscapeException {
    return pump.getAllAlerts();
  }

  public void acknowledge(AuthenticatedUser user, List<Alert> alert) throws AlertscapeException {
    for (Alert a : alert) {
      a.setAcknowledgedBy(user.getUsername());
      LOG.info("Acknowledging alert: " + a.getCompositeAlertId());
      pump.processUprampAlert(a);
    }
  }

  public void unacknowledge(AuthenticatedUser user, List<Alert> alert) throws AlertscapeException {
    for (Alert a : alert) {
      a.setAcknowledgedBy(null);
      LOG.info("Unacknowledging alert: " + a.getCompositeAlertId());
      pump.processUprampAlert(a);
    }
  }

  public void clear(AuthenticatedUser user, List<Alert> alert) throws AlertscapeException {
    for (Alert a : alert) {
      a.setStatus(AlertStatus.CLEARED);
      LOG.info("Clearing alert: " + a.getCompositeAlertId());
      pump.processUprampAlert(a);
    }
  }

  public List<AlertAttributeDefinition> getAttributeDefinitions() throws AlertscapeException {
    return pump.getAttributeDefinitions();
  }

  /**
   * @return the pump
   */
  public AlertPump getPump() {
    return pump;
  }

  /**
   * @param pump
   *          the pump to set
   */
  public void setPump(AlertPump pump) {
    this.pump = pump;
  }
}
