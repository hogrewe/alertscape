/*
 * Created on Mar 26, 2006
 */
package com.alertscape.browser.model;

import java.util.List;

import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;

/**
 * @author josh
 * @version $Version: $
 */
public class AlertChange {
  private AlertChangeType type;
  private List<Alert> alerts;
  private List<Integer> indices;
  private AlertCollection source;

  public AlertChange(AlertChangeType type, List<Alert> events, List<Integer> indices, AlertCollection source) {
    this.type = type;
    this.alerts = events;
    this.indices = indices;
    this.source = source;
  }

  public AlertCollection getSource() {
    return source;
  }

  public List<Alert> getAlerts() {
    return alerts;
  }

  public List<Integer> getIndices() {
    return indices;
  }

  public AlertChangeType getType() {
    return type;
  }

  public enum AlertChangeType {
    INSERT("INSERT"), UPDATE("UPDATE"), REMOVE("REMOVE");

    private String type;

    private AlertChangeType(String type) {
      this.type = type;
    }

    @Override
    public String toString() {
      return type;
    }
  }

}
