/**
 * 
 */
package com.alertscape.common.model;

import java.io.Serializable;

/**
 * @author josh
 * 
 */
public class AlertAttributeDefinition implements Serializable {
  private static final long serialVersionUID = -2507943410147711093L;
  private int attributeDefinitionId;
  private String name;
  private String displayName;
  private boolean active;

  /**
   * @return the attributeDefinitionId
   */
  public int getAttributeDefinitionId() {
    return attributeDefinitionId;
  }

  /**
   * @param attributeDefinitionId
   *          the attributeDefinitionId to set
   */
  public void setAttributeDefinitionId(int attributeDefinitionId) {
    this.attributeDefinitionId = attributeDefinitionId;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(String name) {
    this.name = name;
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
   * @return the displayName
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * @param displayName
   *          the displayName to set
   */
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
}
