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
public class LocalAlertTransport implements AlertTransport {
  private AlertPump pump;

  public void sendAlert(Alert a) throws AlertTransportException {
    try {
      pump.processAlert(a);
    } catch (AlertscapeException e) {
      throw new AlertTransportException("Couldn't send alert to the pump", e);
    }
  }

  public List<Alert> getAlerts(AlertSource source) throws AlertTransportException {
    try {
      return pump.getAlerts(source);
    } catch (AlertscapeException e) {
      throw new AlertTransportException("Couldn't get alerts from the pump", e);
    }
  }

  public AlertPump getPump() {
    return pump;
  }

  public void setPump(AlertPump pump) {
    this.pump = pump;
  }

}