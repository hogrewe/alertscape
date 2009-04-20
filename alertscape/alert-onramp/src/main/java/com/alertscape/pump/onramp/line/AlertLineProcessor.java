/**
 * 
 */
package com.alertscape.pump.onramp.line;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public interface AlertLineProcessor {
  /**
   * @param line
   * @return
   */
  Alert createAlert(String line);

  /**
   * @param line
   * @return
   */
  Alert populateAlert(Alert alert, String line);
  
  boolean matches(String line);
}
