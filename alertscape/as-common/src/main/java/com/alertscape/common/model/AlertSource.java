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
