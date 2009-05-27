/**
 * 
 */
package com.alertscape.browser.model.tree;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class IsEmptyAttributeMatcher implements AlertMatcher {
  private NonEmptyAttributeMatcher delegate = new NonEmptyAttributeMatcher();

  /**
   * @param attributeName
   */
  public IsEmptyAttributeMatcher(String attributeName) {
    setAttributeName(attributeName);
  }

  /**
   * 
   */
  public IsEmptyAttributeMatcher() {
  }

  @Override
  public boolean matches(Alert alert) {
    return !delegate.matches(alert);
  }

  /**
   * @return the attributeName
   */
  public String getAttributeName() {
    return delegate.getAttributeName();
  }

  /**
   * @param attributeName
   *          the attributeName to set
   */
  public void setAttributeName(String attributeName) {
    delegate.setAttributeName(attributeName);
  }

}
