/**
 * 
 */
package com.alertscape.pump.onramp.sender;

import com.alertscape.AlertscapeException;

/**
 * @author josh
 * 
 */
public class AlertTransportException extends AlertscapeException {

  /**
   * 
   */
  private static final long serialVersionUID = 2426723326375266154L;

  /**
   * 
   */
  public AlertTransportException() {
  }

  /**
   * @param message
   */
  public AlertTransportException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public AlertTransportException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public AlertTransportException(String message, Throwable cause) {
    super(message, cause);
  }
}
