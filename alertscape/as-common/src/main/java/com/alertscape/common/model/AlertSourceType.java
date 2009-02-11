/**
 * 
 */
package com.alertscape.common.model;

import java.io.Serializable;

/**
 * @author josh
 * 
 */
public class AlertSourceType implements Serializable {
  private static final long serialVersionUID = -4745480015681872668L;
  private long sid;
  private String type;
  private String mappingFile;

  /**
   * @return the sid
   */
  public long getSid() {
    return sid;
  }

  /**
   * @param sid
   *          the sid to set
   */
  public void setSid(long sid) {
    this.sid = sid;
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
   * @return the mappingFile
   */
  public String getMappingFile() {
    return mappingFile;
  }

  /**
   * @param mappingFile
   *          the mappingFile to set
   */
  public void setMappingFile(String mappingFile) {
    this.mappingFile = mappingFile;
  }
}
