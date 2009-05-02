/**
 * 
 */
package com.alertscape.service;

import java.util.List;
import java.util.Map;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertAttributeDefinition;
import com.alertscape.common.model.AuthenticatedUser;

/**
 * @author josh
 * 
 */
public interface AlertService {

  List<Alert> getAllAlerts(String filter) throws AlertscapeException;

  void clear(AuthenticatedUser user, List<Alert> alert) throws AlertscapeException;

  void unacknowledge(AuthenticatedUser user, List<Alert> alert) throws AlertscapeException;

  void acknowledge(AuthenticatedUser user, List<Alert> alert) throws AlertscapeException;
    
  void categorize(AuthenticatedUser user, List<Alert> alerts, String tagName, String tagValue) throws AlertscapeException;
  
  Map<String,?> getCategories(AuthenticatedUser user) throws AlertscapeException;
  
  void label(AuthenticatedUser user, List<Alert> alerts, String tagName, String tagValue) throws AlertscapeException;
  
  Map<String,List<String>> getLabels(AuthenticatedUser user) throws AlertscapeException;
  
  List<AlertAttributeDefinition> getAttributeDefinitions() throws AlertscapeException;
  
  String getTreeConfiguration() throws AlertscapeException;
}
