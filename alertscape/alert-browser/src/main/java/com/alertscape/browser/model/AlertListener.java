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
  void setCollection(AlertCollection collection);
  void startListening(String selector) throws AlertscapeException;
  void startProcessing() throws AlertscapeException;  
  void stopProcessing() throws AlertscapeException;
  void setExceptionListener(AlertListenerExceptionListener listener);
  /**
   * 
   */
  void disconnect();
}
