/**
 * 
 */
package com.alertscape.pump.onramp.sender;

import java.util.List;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;
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

  public List<Alert> registerAlertSource(AlertSource source, AlertSourceCallback callback) throws AlertTransportException {
    try {
      return pump.registerAlertSource(source, callback);
    } catch (AlertscapeException e) {
      throw new AlertTransportException("Couldn't get alerts from the pump", e);
    }
  }

  public long getNextAlertId(int skipCount) throws AlertTransportException {
    // TODO Auto-generated method stub
    return 0;
  }

  public AlertSource getSource(int sourceId) throws AlertTransportException {
    // TODO Auto-generated method stub
    return null;
  }
  
  public AlertSource getSource(String sourceName) throws AlertTransportException {
    try {
      return pump.getAlertSource(sourceName);
    } catch (AlertscapeException e) {
      throw new AlertTransportException("Couldn't get alert source", e);
    }
  }

  public AlertPump getPump() {
    return pump;
  }

  public void setPump(AlertPump pump) {
    this.pump = pump;
  }

}
