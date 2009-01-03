/**
 * 
 */
package com.alertscape.pump;

import java.util.List;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;
import com.alertscape.pump.offramp.DatabaseOfframp;
import com.alertscape.pump.offramp.JmsOfframp;

/**
 * @author josh
 * 
 */
public class ConfigurableAlertPump implements AlertPump {
  private DatabaseOfframp dbOfframp;
  private JmsOfframp jmsOfframp;

  public void processAlert(Alert a) throws AlertscapeException {
    if (dbOfframp != null) {
      dbOfframp.processAlert(a);
    }
    if (jmsOfframp != null) {
      jmsOfframp.processAlert(a);
    }
  }

  public List<Alert> getAlerts(AlertSource source) throws AlertscapeException {
    return getDbOfframp().getAlertsForSource(source);
  }

  public List<Alert> getAllAlerts() throws AlertscapeException {
    return getDbOfframp().getAllAlerts();
  }

  /**
   * @return the dbOfframp
   */
  public DatabaseOfframp getDbOfframp() {
    return dbOfframp;
  }

  /**
   * @param dbOfframp
   *          the dbOfframp to set
   */
  public void setDbOfframp(DatabaseOfframp dbOfframp) {
    this.dbOfframp = dbOfframp;
  }

  /**
   * @return the jmsOfframp
   */
  public JmsOfframp getJmsOfframp() {
    return jmsOfframp;
  }

  /**
   * @param jmsOfframp
   *          the jmsOfframp to set
   */
  public void setJmsOfframp(JmsOfframp jmsOfframp) {
    this.jmsOfframp = jmsOfframp;
  }

}
