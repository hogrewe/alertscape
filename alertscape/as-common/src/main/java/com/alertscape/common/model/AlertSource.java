/**
 * 
 */
package com.alertscape.common.model;

import java.io.Serializable;


/**
 * @author josh
 * 
 */
public class AlertSource implements Serializable {
  private static final long serialVersionUID = -5824147623789511142L;
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
	
	public String toString() {
	  return sourceName;
	}
}
