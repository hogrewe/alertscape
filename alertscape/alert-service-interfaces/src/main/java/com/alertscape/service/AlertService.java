/**
 * 
 */
package com.alertscape.service;

import java.util.List;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AuthenticatedUser;

/**
 * @author josh
 * 
 */
public interface AlertService {

  public List<Alert> getAllAlerts() throws AlertscapeException;
  public void clear(AuthenticatedUser user, List<Alert> alert) throws AlertscapeException;
  public void unacknowledge(AuthenticatedUser user, List<Alert> alert) throws AlertscapeException;
  public void acknowledge(AuthenticatedUser user, List<Alert> alert) throws AlertscapeException;
}
