/**
 * 
 */
package com.alertscape.pump.onramp;

import java.util.HashMap;
import java.util.Map;

import com.alertscape.common.model.Alert;
import com.alertscape.pump.onramp.equator.AlertEquator;
import com.alertscape.pump.onramp.sender.AlertSender;
import com.alertscape.pump.onramp.sender.AlertSendingException;

/**
 * @author josh
 * 
 */
public final class AlertOnramp {
  private AlertSender sender;
  private long nextAlertId;
  private byte[] nextAlertIdLock = new byte[0];
  private AlertEquator equator;
  private Map<AlertDedupWrapper, Alert> alertMap = new HashMap<AlertDedupWrapper, Alert>();

  public void sendAlert(Alert alert) {
    AlertDedupWrapper alertWrapper = new AlertDedupWrapper(equator, alert);
    Alert existing = alertMap.get(alertWrapper);
    
    if (existing != null) {
      alert.setAlertId(existing.getAlertId());
      alert.setCount(existing.getCount() + 1);
    } else {
      synchronized (nextAlertIdLock) {
        alert.setAlertId(nextAlertId++);
      }
    }
    
    try {
      sender.sendAlert(alert);
    } catch (AlertSendingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
