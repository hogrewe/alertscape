/**
 * 
 */
package com.alertscape.service;

import java.util.List;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;
import com.alertscape.pump.AlertPump;

/**
 * @author josh
 *
 */
public class ConfigurableAlertService implements AlertService {
  public AlertPump pump;

  public List<Alert> getAllAlerts() throws AlertscapeException {
    return pump.getAllAlerts();
  }

  /**
   * @return the pump
   */
  public AlertPump getPump() {
    return pump;
  }

  /**
   * @param pump the pump to set
   */
  public void setPump(AlertPump pump) {
    this.pump = pump;
  }

}
