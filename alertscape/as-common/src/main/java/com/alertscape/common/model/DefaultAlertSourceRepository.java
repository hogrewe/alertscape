/**
 * 
 */
package com.alertscape.common.model;

import java.util.Map;

/**
 * @author josh
 * 
 */
public class DefaultAlertSourceRepository implements AlertSourceRepository {
  private static final AlertSource DEFAULT_SOURCE = new AlertSource(1, "UNKNOWN");
  private Map<String, AlertSource> alertSources;

  public DefaultAlertSourceRepository(Map<String, AlertSource> alertSources) {
    this.alertSources = alertSources;
  }

  public AlertSource getAlertSource(String name) {
    AlertSource alertSource = alertSources.get(name);
    
    return alertSource == null ? DEFAULT_SOURCE : alertSource;
  }
}
