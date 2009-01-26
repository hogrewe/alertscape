package com.alertscape.browser.model.tree;

import ca.odell.glazedlists.matchers.Matcher;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class AttributeAlertMatcher implements Matcher<Alert> {
  /**
   * 
   */
  private Object value;
  private String attributeName;

  /**
   * @param value
   */
  public AttributeAlertMatcher(String attributeName, Object value) {
    this.attributeName = attributeName;
    this.value = value;
  }

  public boolean matches(Alert alert) {
    return value != null && value.equals(alert.getExtendedAttribute(attributeName));
  }
}