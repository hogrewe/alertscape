/**
 * 
 */
package com.alertscape.pump;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Event;
import com.alertscape.common.model.EventCollection;

/**
 * @author josh
 *
 */
public class ConfigurableAlertPump implements AlertPump {
	EventCollection collection;
	
	public void processAlert(Event e) throws AlertscapeException {
		
	}

	/**
	 * @return the collection
	 */
	public EventCollection getCollection() {
		return collection;
	}

	/**
	 * @param collection the collection to set
	 */
	public void setCollection(EventCollection collection) {
		this.collection = collection;
	}

}
