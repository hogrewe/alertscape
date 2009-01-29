/**
 * 
 */
package com.alertscape.pump.onramp.sender;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.equator.AlertEquator;
import com.alertscape.pump.AlertPump;
import com.alertscape.pump.AlertSourceCallback;

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

  public void registerAlertSource(AlertSource source, AlertSourceCallback callback) throws AlertTransportException {
    try {
      pump.registerAlertSource(source, callback);
    } catch (AlertscapeException e) {
      throw new AlertTransportException("Couldn't get alerts from the pump", e);
    }
  }

  public long getNextAlertId(AlertSource source) throws AlertTransportException {
    return 0;
  }

  public AlertSource getSource(String sourceName) throws AlertTransportException {
    try {
      return pump.getAlertSource(sourceName);
    } catch (AlertscapeException e) {
      throw new AlertTransportException("Couldn't get alert source", e);
    }
  }

  public Alert getAlert(AlertSource source, long alertId) throws AlertTransportException {
    try {
      return pump.getAlert(source, alertId);
    } catch (AlertscapeException e) {
      throw new AlertTransportException("Couldn't get alert: " + source + ", " + alertId);
    }
  }

  public Alert getAlert(Alert a, AlertEquator equator) {
    return pump.getAlert(a, equator);
  }

  public AlertPump getPump() {
    return pump;
  }

  public void setPump(AlertPump pump) {
    this.pump = pump;
  }

}
