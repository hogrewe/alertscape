/**
 * 
 */
package com.alertscape.pump.onramp.file;

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

}
