/**
 * 
 */
package com.alertscape.service;

import java.util.List;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;

/**
 * @author josh
 *
 */
public interface AlertService {
  public List<Alert> getAllAlerts() throws AlertscapeException;
}
