/**
 * 
 */
package com.alertscape.pump;

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
  private static final long SOURCE_MULTIPLIER = 1000000000000000000L;
  private DatabaseOfframp dbOfframp;
  private JmsOfframp jmsOfframp;

  public void processAlert(Alert a) throws AlertscapeException {
    AlertSource source = a.getSource();
    long alertId = a.getAlertId();
    
    // Calculate the prefix for the alert source
    alertId = source.getSourceId() * SOURCE_MULTIPLIER + alertId;
    
    dbOfframp.processAlert(a);
    if (jmsOfframp != null) {
      jmsOfframp.processAlert(a);
    }
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
