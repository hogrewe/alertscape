/*
 * Created on Mar 15, 2006
 */
package com.alertscape.common.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alertscape.common.model.severity.Severity;

/**
 * @author josh
 * @version $Version: $
 */
public class Alert {
	public enum AlertStatus {
		STANDING, CLEARED;
	}

	private long alertId;
	private AlertStatus status = AlertStatus.STANDING;
	private String shortDescription;
	private String longDescription;
	private String type;
	private Severity severity;
	private long count;
	private AlertSource source;
	private String item;
	private String itemManager;
	private String itemType;
	private String itemManagerType;
	private Date firstOccurence;
	private Date lastOccurence;

	private Map<String, Object> majorTags;
	private Map<String, Object> minorTags;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public Date getFirstOccurence() {
		return firstOccurence;
	}

	public void setFirstOccurence(Date firstOccurence) {
		this.firstOccurence = firstOccurence;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getItemManager() {
		return itemManager;
	}

	public void setItemManager(String itemManager) {
		this.itemManager = itemManager;
	}

	public String getItemManagerType() {
		return itemManagerType;
	}

	public void setItemManagerType(String itemManagerType) {
		this.itemManagerType = itemManagerType;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public Date getLastOccurence() {
		return lastOccurence;
	}

	public void setLastOccurence(Date lastOccurence) {
		this.lastOccurence = lastOccurence;
	}

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	/**
	 * @return the source
	 */
	public AlertSource getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(AlertSource source) {
		this.source = source;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isStanding() {
		return getStatus() == AlertStatus.STANDING;
	}

	public long getAlertId() {
		return alertId;
	}

	public void setAlertId(long alertId) {
		this.alertId = alertId;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public AlertStatus getStatus() {
		return status;
	}

	public void setStatus(AlertStatus status) {
		this.status = status;
	}

	/**
	 * @return the majorTags
	 */
	public Map<String, Object> getMajorTags() {
		return majorTags;
	}

	/**
	 * @param majorTags
	 *            the majorTags to set
	 */
	public void setMajorTags(Map<String, Object> majorTags) {
		this.majorTags = majorTags;
		if(this.majorTags == null) {
			this.majorTags = new HashMap<String, Object>();
		}
	}
	
	public void addMajorTag(String name, Object value) {
		majorTags.put(name, value);
	}
	
	public Object getMajorTag(String name) {
		return majorTags.get(name);
	}

	/**
	 * @return the minorTags
	 */
	public Map<String, Object> getMinorTags() {
		return minorTags;
	}

	/**
	 * @param minorTags
	 *            the minorTags to set
	 */
	public void setMinorTags(Map<String, Object> minorTags) {
		this.minorTags = minorTags;
		if(this.minorTags == null) {
			this.minorTags = new HashMap<String, Object>();
		}
	}
	
	public void addMinorTag(String name, Object value) {
		minorTags.put(name, value);
	}
	
	public Object getMinorTag(String name) {
		return minorTags.get(name);
	}

	@Override
	public boolean equals(Object o) {
		return getAlertId() == ((Alert) o).getAlertId();
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getAlertId()).hashCode();
	}

}
