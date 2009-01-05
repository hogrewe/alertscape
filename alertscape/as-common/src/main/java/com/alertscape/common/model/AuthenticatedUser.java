/**
 * 
 */
package com.alertscape.common.model;

import java.io.Serializable;

/**
 * @author josh
 *
 */
public class AuthenticatedUser implements Serializable {
  private static final long serialVersionUID = 916173486049113153L;
  private long userId;
  private String username;
  private String email;
  private String fullName;
  private String sessionId;
  /**
   * @return the userId
   */
  public long getUserId() {
    return userId;
  }
  /**
   * @param userId the userId to set
   */
  public void setUserId(long userId) {
    this.userId = userId;
  }
  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }
  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }
  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }
  /**
   * @param email the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }
  /**
   * @return the fullName
   */
  public String getFullName() {
    return fullName;
  }
  /**
   * @param fullName the fullName to set
   */
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }
  /**
   * @return the sessionId
   */
  public String getSessionId() {
    return sessionId;
  }
  /**
   * @param sessionId the sessionId to set
   */
  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
  
  
}
