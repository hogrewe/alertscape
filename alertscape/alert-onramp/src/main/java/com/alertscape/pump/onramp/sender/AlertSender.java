/**
 * 
 */
package com.alertscape.pump.onramp.sender;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 *
 */
public interface AlertSender {
  void sendAlert(Alert a) throws AlertSendingException;
}
