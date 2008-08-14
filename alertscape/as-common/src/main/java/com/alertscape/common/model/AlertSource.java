/**
 * 
 */
package com.alertscape.common.model;


/**
 * @author josh
 * 
 */
public class AlertSource {
	private int sourceId;
	private String sourceName;

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
