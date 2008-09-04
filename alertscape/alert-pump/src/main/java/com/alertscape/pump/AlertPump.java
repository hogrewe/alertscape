/**
 * 
 */
package com.alertscape.pump;

import java.util.List;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;

/**
 * @author josh
 *
 */
public interface AlertPump {
	public void processAlert(Alert a) throws AlertscapeException;
	public List<Alert> getAlerts(AlertSource source) throws AlertscapeException;
}
