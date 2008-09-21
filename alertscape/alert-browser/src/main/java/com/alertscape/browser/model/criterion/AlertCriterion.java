/*
 * Created on Mar 15, 2006
 */
package com.alertscape.browser.model.criterion;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * @version $Version: $
 */
public interface AlertCriterion {
  /**
   * This will test the <code>Alert</code> to determine if it matches this criterion.
   * 
   * @param a
   *          The <code>Alert</code> to test
   * @return true if the <code>Alert</code> matches this criterion, false otherwise
   */
  public boolean matches(Alert a);
}
