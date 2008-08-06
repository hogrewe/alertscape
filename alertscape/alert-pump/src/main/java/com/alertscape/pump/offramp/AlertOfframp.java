/**
 * 
 */
package com.alertscape.pump.offramp;

import com.alertscape.common.model.Event;

/**
 * @author josh
 *
 */
public interface AlertOfframp {
	public void processAlert(Event alert);
}
