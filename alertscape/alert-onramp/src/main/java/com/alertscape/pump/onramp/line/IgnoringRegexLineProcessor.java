/**
 * 
 */
package com.alertscape.pump.onramp.line;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 */
public class IgnoringRegexLineProcessor extends AbstractRegexLineProcessor {

  @Override
  public Alert createAlert(String line) {
    return null;
  }

  @Override
  public Alert populateAlert(Alert alert, String line) {
    return null;
  }
}
