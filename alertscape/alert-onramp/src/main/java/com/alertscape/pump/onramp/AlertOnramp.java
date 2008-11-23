/**
 * 
 */
package com.alertscape.pump.onramp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;
import com.alertscape.pump.onramp.equator.AlertEquator;
import com.alertscape.pump.onramp.sender.AlertTransport;
import com.alertscape.pump.onramp.sender.AlertTransportException;

/**
 * @author josh
 * 
 */
public abstract class AlertOnramp {
  private AlertTransport transport;
  private long nextAlertId;
  private byte[] nextAlertIdLock = new byte[0];
  private AlertEquator equator;
  private Map<AlertDedupWrapper, Alert> alertMap = new HashMap<AlertDedupWrapper, Alert>();
  private AlertSource source = new AlertSource(1, "UNKNOWN");

  public void sendAlert(Alert alert) {
    alert.setSource(source);
    AlertDedupWrapper alertWrapper = new AlertDedupWrapper(equator, alert);
    Alert existing;
    synchronized (alertMap) {
      existing = alertMap.get(alertWrapper);
      alertMap.put(alertWrapper, alert);
    }
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
      transport.sendAlert(alert);
    } catch (AlertTransportException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public final void onrampInit() {
    try {
      List<Alert> alerts = transport.getAlerts(source);
      for (Alert alert : alerts) {
        // TODO: store this somewhere else
        long trueId = alert.getAlertId() % 100000000000l;
        if(trueId > nextAlertId) {
          nextAlertId = trueId + 1;
        }
        AlertDedupWrapper alertWrapper = new AlertDedupWrapper(equator, alert);
        alertMap.put(alertWrapper, alert);
      }
    } catch (AlertTransportException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    init();

    // TODO: take this out please
    // Thread t = new Thread() {
    //
    // @Override
    // public void run() {
    // Severity sev = SeverityFactory.getInstance().getSeverity(2);
    // while (true) {
    // Alert alert = new Alert();
    // alert.setSeverity(sev);
    // alert.setSource(source);
    // sendAlert(alert);
    // try {
    // sleep(100);
    // } catch (InterruptedException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    // }
    // };
    //
    // t.setName("AlertSendingTester");
    // t.setDaemon(true);
    // t.start();
  }

  protected abstract void init();

  public AlertTransport getTransport() {
    return transport;
  }

  public void setTransport(AlertTransport sender) {
    this.transport = sender;
  }

  public AlertEquator getEquator() {
    return equator;
  }

  public void setEquator(AlertEquator equator) {
    this.equator = equator;
  }

  /**
   * @return the source
   */
  public AlertSource getSource() {
    return source;
  }

  /**
   * @param source
   *          the source to set
   */
  public void setSource(AlertSource source) {
    this.source = source;
  }

}
