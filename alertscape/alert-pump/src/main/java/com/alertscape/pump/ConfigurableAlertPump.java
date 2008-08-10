/**
 * 
 */
package com.alertscape.pump;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;
import com.alertscape.pump.offramp.DatabaseOfframp;
import com.alertscape.pump.offramp.JmsOfframp;

/**
 * @author josh
 * 
 */
public class ConfigurableAlertPump implements AlertPump {
	private DatabaseOfframp dbOfframp;
	private JmsOfframp jmsOfframp;
	private Set<AlertSource> sources = new HashSet<AlertSource>();
	private Map<AlertSource, Set<Alert>> sourceToAlertsMap = new HashMap<AlertSource, Set<Alert>>();

	public void processAlert(Alert a) throws AlertscapeException {
		AlertSource source = a.getSource();
		Set<Alert> alerts = sourceToAlertsMap.get(source);
		if(alerts == null) {
			alerts = new HashSet<Alert>();
			sourceToAlertsMap.put(source, alerts);
		}
	}

	/**
	 * @return the dbOfframp
	 */
	public DatabaseOfframp getDbOfframp() {
		return dbOfframp;
	}

	/**
	 * @param dbOfframp
	 *            the dbOfframp to set
	 */
	public void setDbOfframp(DatabaseOfframp dbOfframp) {
		this.dbOfframp = dbOfframp;
	}

	/**
	 * @return the jmsOfframp
	 */
	public JmsOfframp getJmsOfframp() {
		return jmsOfframp;
	}

	/**
	 * @param jmsOfframp
	 *            the jmsOfframp to set
	 */
	public void setJmsOfframp(JmsOfframp jmsOfframp) {
		this.jmsOfframp = jmsOfframp;
	}
}
