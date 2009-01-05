/**
 * 
 */
package com.alertscape.dao;

import com.alertscape.AlertscapeException;

/**
 * @author josh
 *
 */
public class DaoException extends AlertscapeException {
	private static final long serialVersionUID = -8583412058357312121L;

	/**
	 * 
	 */
	public DaoException() {
	}

	/**
	 * @param message
	 */
	public DaoException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DaoException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

}
