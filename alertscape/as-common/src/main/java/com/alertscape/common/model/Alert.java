/*
 * Created on Mar 15, 2006
 */
package com.alertscape.common.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.alertscape.common.model.severity.Severity;

/**
 * @author josh
 * @version $Version: $
 */
public class Alert implements Serializable {
  private static final long COMBINED_ID_MULTIPLIER = (long) Math.pow(10, 15);
  private static final long serialVersionUID = -1396527085374975232L;

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
  private long compositeAlertId = -1;
  private String acknowledgedBy;

  private Map<String, Object> majorTags;
  private Map<String, Object> minorTags;
  private Map<String, Object> extendedAttributes;

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
   * @param source
   *          the source to set
   */
  public void setSource(AlertSource source) {
    this.source = source;
    this.compositeAlertId = -1;
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
    this.compositeAlertId = -1;
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

  public void setStatus(String status) {
    this.status = AlertStatus.valueOf(status);
  }

  /**
   * @return the majorTags
   */
  public Map<String, Object> getMajorTags() {
    if (this.majorTags == null) {
      this.majorTags = new HashMap<String, Object>();
    }
    return majorTags;
  }

  /**
   * @param majorTags
   *          the majorTags to set
   */
  public void setMajorTags(Map<String, Object> majorTags) {
    this.majorTags = majorTags;
    if (this.majorTags == null) {
      this.majorTags = new HashMap<String, Object>();
    }
  }

  public void addMajorTag(String name, Object value) {
    getMajorTags().put(name, value);
  }

  public Object getMajorTag(String name) {
    return majorTags.get(name);
  }

  /**
   * @return the minorTags
   */
  public Map<String, Object> getMinorTags() {
    if (this.minorTags == null) {
      this.minorTags = new HashMap<String, Object>();
    }

    return minorTags;
  }

  /**
   * @param minorTags
   *          the minorTags to set
   */
  public void setMinorTags(Map<String, Object> minorTags) {
    this.minorTags = minorTags;
    if (this.minorTags == null) {
      this.minorTags = new HashMap<String, Object>();
    }
  }

  public void addMinorTag(String name, Object value) {
    getMinorTags().put(name, value);
  }

  public Object getMinorTag(String name) {
    return minorTags.get(name);
  }

  /**
   * @return the compositeAlertId
   */
  public long getCompositeAlertId() {
    if (compositeAlertId <= 0) {
      compositeAlertId = (source == null ? 0 : source.getSourceId() * COMBINED_ID_MULTIPLIER) + alertId;
    }
    return compositeAlertId;
  }

  /**
   * @return the acknowledgedBy
   */
  public String getAcknowledgedBy() {
    return acknowledgedBy;
  }

  /**
   * @param acknowledgedBy
   *          the acknowledgedBy to set
   */
  public void setAcknowledgedBy(String acknowledgedBy) {
    this.acknowledgedBy = acknowledgedBy;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (alertId ^ (alertId >>> 32));
    result = prime * result + ((source == null) ? 0 : source.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Alert other = (Alert) obj;
    if (alertId != other.alertId)
      return false;
    if (source == null) {
      if (other.source != null)
        return false;
    } else if (!source.equals(other.source))
      return false;
    return true;
  }

  /**
   * @return the extendedAttributes
   */
  public Object getExtendedAttribute(String name) {
    return extendedAttributes == null ? null : extendedAttributes.get(name);
  }

  public Map<String, Object> getExtendedAttributes() {
    if (this.extendedAttributes == null) {
      this.extendedAttributes = new HashMap<String, Object>();
    }
    return extendedAttributes;
  }

  /**
   * @param extendedAttributes
   *          the extendedAttributes to set
   */
  public void setExtendedAttributes(Map<String, Object> extendedAttributes) {
    this.extendedAttributes = extendedAttributes;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("source", source).append("alertId", alertId).append("item", item).append(
        "itemType", itemType).append("itemManager", itemManager).append("itemManagerType", itemManagerType).append(
        "acknowledgedBy", acknowledgedBy).append("count", count).append("exgtendedAttributes", extendedAttributes)
        .append("firstOccurence", firstOccurence).append("lastOccurence", lastOccurence).append("longDescription",
            longDescription).append("severity", severity).append("shortDescription", shortDescription).append("status",
            status).append("type", type).toString();
  }

}
