/**
 * 
 */
package com.alertscape.web.ui.admin.client.model;

import java.io.Serializable;

/**
 * @author josh
 * 
 */
public class OnrampDefinition implements Serializable {
  private static final long serialVersionUID = -5572459708351572031L;

  private int id;
  private String name;
  private String configuration;
  private String type;

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
   * @return the configuration
   */
  public String getConfiguration() {
    return configuration;
  }

  /**
   * @param configuration
   *          the configuration to set
   */
  public void setConfiguration(String configuration) {
    this.configuration = configuration;
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @param type
   *          the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

}
