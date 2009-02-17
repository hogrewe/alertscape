/**
 * 
 */
package com.alertscape.pump.onramp.file;

import java.util.regex.Matcher;

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
  Alert createAlert(Matcher matcher);

  /**
   * @param line
   * @return
   */
  Alert populateAlert(Alert alert, Matcher matcher);

}
