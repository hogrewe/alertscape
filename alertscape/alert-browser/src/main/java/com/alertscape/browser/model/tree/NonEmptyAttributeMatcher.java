/**
 * 
 */
package com.alertscape.browser.model.tree;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class NonEmptyAttributeMatcher implements AlertMatcher {
  private String attributeName;

  public NonEmptyAttributeMatcher(String attributeName) {
    this.attributeName = attributeName;
  }
  
  public NonEmptyAttributeMatcher( ) {
    
  }

  public boolean matches(Alert alert) {
    return alert.getExtendedAttribute(attributeName) != null;
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
