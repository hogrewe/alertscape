/**
 * 
 */
package com.alertscape.pump;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;

/**
 * @author josh
 *
 */
public interface AlertPump {
	public void processAlert(Alert e) throws AlertscapeException;
}
