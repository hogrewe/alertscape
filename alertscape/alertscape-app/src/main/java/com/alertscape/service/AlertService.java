/**
 * 
 */
package com.alertscape.service;

import java.util.Collection;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;

/**
 * @author josh
 *
 */
public interface AlertService {
  public Collection<Alert> getAllAlerts() throws AlertscapeException;
}
