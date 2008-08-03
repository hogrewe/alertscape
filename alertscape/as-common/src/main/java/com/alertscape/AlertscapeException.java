/**
 * 
 */
package com.alertscape;

/**
 * @author josh
 *
 */
public class AlertscapeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9102015399954224324L;

	/**
	 * 
	 */
	public AlertscapeException() {
		super();
	}

	/**
	 * @param message
	 */
	public AlertscapeException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public AlertscapeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AlertscapeException(String message, Throwable cause) {
		super(message, cause);
	}

}
