/**
 * 
 */
package com.alertscape.pump;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Event;

/**
 * @author josh
 *
 */
public interface AlertPump {
	public void addAlert(Event e) throws AlertscapeException;
}
