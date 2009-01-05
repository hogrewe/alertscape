/**
 * 
 */
package com.alertscape.browser.upramp.firstparty.unack;

import java.util.List;
import java.util.Map;

import com.alertscape.browser.model.BrowserContext;
import com.alertscape.browser.ui.swing.AlertBrowser;
import com.alertscape.browser.upramp.model.UpRamp;
import com.alertscape.common.model.Alert;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author josh
 *
 */
public class UnacknowledgeUpramp implements UpRamp {

  public Map initialize(BrowserContext context) {
    return Collections.emptyMap();
  }

  public boolean isAuthorized(BrowserContext context) {
    return true;
  }

  public boolean submit(BrowserContext context, Map keyvals) {
    List<Alert> alerts = context.getSelectedAlerts();
    
    AlertBrowser.unacknowledge(alerts);
    return true;
  }
}
