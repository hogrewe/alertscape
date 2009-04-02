/**
 * 
 */
package com.alertscape.browser.model.tree;

import java.util.List;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class CompositeAlertMatcher implements AlertMatcher {
  private List<AlertMatcher> matchers;
  private MatchingMode mode;

  public enum MatchingMode {
    AND, OR
  };

  @Override
  public boolean matches(Alert a) {
    for (AlertMatcher matcher : matchers) {
      boolean matches = matcher.matches(a);
      if (mode == MatchingMode.AND && !matches) {
        return false;
      } else if (mode == MatchingMode.OR && matches) {
        return true;
      }
    }
    if (mode == MatchingMode.AND) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * @return the matchers
   */
  public List<AlertMatcher> getMatchers() {
    return matchers;
  }

  /**
   * @param matchers
   *          the matchers to set
   */
  public void setMatchers(List<AlertMatcher> matchers) {
    this.matchers = matchers;
  }

  /**
   * @return the mode
   */
  public MatchingMode getMode() {
    return mode;
  }

  /**
   * @param mode
   *          the mode to set
   */
  public void setMode(MatchingMode mode) {
    this.mode = mode;
  }

  public void setModeAsString(String mode) {
    this.mode = MatchingMode.valueOf(mode);
  }

  public String getModeAsString() {
    return mode.toString();
  }
}
