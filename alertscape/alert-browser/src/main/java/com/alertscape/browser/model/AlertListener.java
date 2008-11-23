/**
 * 
 */
package com.alertscape.browser.model;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.AlertCollection;

/**
 * @author josh
 *
 */
public interface AlertListener {
  public void setCollection(AlertCollection collection);
  public void startListening() throws AlertscapeException;
}
