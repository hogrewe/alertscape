/**
 * 
 */
package com.alertscape.pump.onramp.sender;

import java.util.List;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;
import com.alertscape.pump.AlertPump;

/**
 * @author josh
 * 
 */
public class LocalAlertSender implements AlertSender {
  private AlertPump pump;

  public void sendAlert(Alert a) throws AlertSendingException {
    try {
      pump.processAlert(a);
    } catch (AlertscapeException e) {
      throw new AlertSendingException("Couldn't send alert to the pump", e);
    }
  }

  public List<Alert> getAlerts(AlertSource source) throws AlertSendingException {
    try {
      return pump.getAlerts(source);
    } catch (AlertscapeException e) {
      throw new AlertSendingException("Couldn't get alerts from the pump", e);
    }
  }

  public AlertPump getPump() {
    return pump;
  }

  public void setPump(AlertPump pump) {
    this.pump = pump;
  }

}
