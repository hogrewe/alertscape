/**
 * 
 */
package com.alertscape.pump.onramp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.severity.Severity;
import com.alertscape.common.model.severity.SeverityFactory;
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
    Date now = new Date();

    if (existing != null) {
      alert.setAlertId(existing.getAlertId());
      alert.setCount(existing.getCount() + 1);
      alert.setFirstOccurence(existing.getFirstOccurence());
    } else {
      synchronized (nextAlertIdLock) {
        alert.setAlertId(nextAlertId++);
      }
      alert.setCount(1);
      alert.setFirstOccurence(now);
    }

    alert.setLastOccurence(now);

    try {
      sender.sendAlert(alert);
    } catch (AlertSendingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  // TODO: take this out please
  public void init() {
    Thread t = new Thread() {

      @Override
      public void run() {
        Severity sev = SeverityFactory.getInstance().getSeverity(2);
        AlertSource source = new AlertSource(0, "UNKNOWN");
        while (true) {
          Alert alert = new Alert();
          alert.setSeverity(sev);
          alert.setSource(source);
          sendAlert(alert);
          try {
            sleep(100);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
    };

    t.setName("AlertSendingTester");
    t.setDaemon(true);
    t.start();
  }

  public AlertSender getSender() {
    return sender;
  }

  public void setSender(AlertSender sender) {
    this.sender = sender;
  }

  public AlertEquator getEquator() {
    return equator;
  }

  public void setEquator(AlertEquator equator) {
    this.equator = equator;
  }
}
