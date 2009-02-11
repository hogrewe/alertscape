/**
 * 
 */
package com.alertscape.common.model;

import java.io.Serializable;

/**
 * @author josh
 * 
 */
public class AlertSource implements Serializable, Comparable<AlertSource> {
  private static final long serialVersionUID = -5824147623789511142L;
  private int sourceId;
  private String sourceName;
  private String configXml;
  private AlertSourceType type;
  private boolean active;

  public AlertSource(int sourceId, String sourceName) {
    this.sourceId = sourceId;
    this.sourceName = sourceName;
  }

  /**
   * @return the active
   */
  public boolean isActive() {
    return active;
  }

  /**
   * @param active
   *          the active to set
   */
  public void setActive(boolean active) {
    this.active = active;
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

  /**
   * @return the configXml
   */
  public String getConfigXml() {
    return configXml;
  }

  /**
   * @param configXml
   *          the configXml to set
   */
  public void setConfigXml(String configXml) {
    this.configXml = configXml;
  }

  /**
   * @return the type
   */
  public AlertSourceType getType() {
    return type;
  }

  /**
   * @param type
   *          the type to set
   */
  public void setType(AlertSourceType type) {
    this.type = type;
  }

  public String toString() {
    return sourceName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + sourceId;
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
    AlertSource other = (AlertSource) obj;
    if (sourceId != other.sourceId)
      return false;
    return true;
  }

  public int compareTo(AlertSource o) {
    return getSourceName().compareTo(o.getSourceName());
  }
}
