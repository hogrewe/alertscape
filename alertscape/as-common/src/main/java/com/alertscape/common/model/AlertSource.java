/**
 * 
 */
package com.alertscape.common.model;

import java.util.Comparator;

/**
 * @author josh
 * 
 */
public class AlertSource {
	private int sourceId;
	private String sourceName;
	private Comparator<Alert> alertComparator;
	
	/**
	 * @return the alertComparator
	 */
	public Comparator<Alert> getAlertComparator() {
		return alertComparator;
	}

	/**
	 * @param alertComparator the alertComparator to set
	 */
	public void setAlertComparator(Comparator<Alert> alertComparator) {
		this.alertComparator = alertComparator;
	}

	public AlertSource(int sourceId, String sourceName) {
		this.sourceId = sourceId;
		this.sourceName = sourceName;
	}

	/**
	 * @return the sourceId
	 */
	public int getSourceId() {
		return sourceId;
	}

	/**
	 * @return the sourceName
	 */
	public String getSourceName() {
		return sourceName;
	}
}
