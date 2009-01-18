/**
 * 
 */
package com.alertscape.pump.offramp;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;

/**
 * @author josh
 *
 */
public interface AlertOfframp {
	public void processAlert(Alert alert) throws AlertscapeException;
	public String getOfframpName();
}
