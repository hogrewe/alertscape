/**
 * 
 */
package com.alertscape.pump.onramp.sender;

import com.alertscape.AlertscapeException;

/**
 * @author josh
 * 
 */
public class AlertSendingException extends AlertscapeException {

  /**
   * 
   */
  private static final long serialVersionUID = 2426723326375266154L;

  /**
   * 
   */
  public AlertSendingException() {
  }

  /**
   * @param message
   */
  public AlertSendingException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public AlertSendingException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public AlertSendingException(String message, Throwable cause) {
    super(message, cause);
  }
}
