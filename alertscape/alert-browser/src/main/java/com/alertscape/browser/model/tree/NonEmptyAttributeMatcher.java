/**
 * 
 */
package com.alertscape.browser.model.tree;

import ca.odell.glazedlists.matchers.Matcher;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class NonEmptyAttributeMatcher implements Matcher<Alert> {
  private String attributeName;

  public NonEmptyAttributeMatcher(String attributeName) {
    this.attributeName = attributeName;
  }

  public boolean matches(Alert alert) {
    return alert.getExtendedAttribute(attributeName) != null;
  }

}
