/**
 * 
 */
package com.alertscape.pump;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Event;
import com.alertscape.pump.offramp.DatabaseOfframp;
import com.alertscape.pump.offramp.JmsOfframp;

/**
 * @author josh
 *
 */
public class ConfigurableAlertPump implements AlertPump {
	public DatabaseOfframp dbOfframp;
	public JmsOfframp jmsOfframp;
	
	public void processAlert(Event e) throws AlertscapeException {
		
	}

}
