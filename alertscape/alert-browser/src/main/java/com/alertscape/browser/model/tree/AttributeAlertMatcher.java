package com.alertscape.browser.model.tree;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class AttributeAlertMatcher implements AlertMatcher {
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
  
  public AttributeAlertMatcher() {
    
  }

  public boolean matches(Alert alert) {
    return value != null && value.equals(alert.getExtendedAttribute(attributeName));
  }

  /**
   * @return the value
   */
  public Object getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(Object value) {
    this.value = value;
  }

  /**
   * @return the attributeName
   */
  public String getAttributeName() {
    return attributeName;
  }

  /**
   * @param attributeName the attributeName to set
   */
  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }
}