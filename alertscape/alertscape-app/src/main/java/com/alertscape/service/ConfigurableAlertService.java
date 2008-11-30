/**
 * 
 */
package com.alertscape.service;

import java.util.Collection;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;
import com.alertscape.pump.AlertPump;

/**
 * @author josh
 *
 */
public class ConfigurableAlertService implements AlertService {
  public AlertPump pump;

  public Collection<Alert> getAllAlerts() throws AlertscapeException {
    return pump.getAllAlerts();
  }

}
