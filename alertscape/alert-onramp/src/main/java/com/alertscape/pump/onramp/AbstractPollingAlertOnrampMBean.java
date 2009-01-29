/**
 * 
 */
package com.alertscape.pump.onramp;

/**
 * @author josh
 *
 */
public interface AbstractPollingAlertOnrampMBean {
  void saveState();
  void stop();
  void start();
  Object showState();
  long showAlertsProcessed();
  void clearAlertsProcessed();
}
