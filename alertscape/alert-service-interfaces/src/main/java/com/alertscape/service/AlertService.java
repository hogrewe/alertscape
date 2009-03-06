/**
 * 
 */
package com.alertscape.service;

import java.util.List;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertAttributeDefinition;
import com.alertscape.common.model.AuthenticatedUser;

/**
 * @author josh
 * 
 */
public interface AlertService {

  public List<Alert> getAllAlerts(String filter) throws AlertscapeException;

  public void clear(AuthenticatedUser user, List<Alert> alert) throws AlertscapeException;

  public void unacknowledge(AuthenticatedUser user, List<Alert> alert) throws AlertscapeException;

  public void acknowledge(AuthenticatedUser user, List<Alert> alert) throws AlertscapeException;

  public List<AlertAttributeDefinition> getAttributeDefinitions() throws AlertscapeException;
  
  public String getTreeConfiguration() throws AlertscapeException;
}
