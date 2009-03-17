/**
 * 
 */
package com.alertscape.wizard.client.model;

import java.io.Serializable;

/**
 * @author josh
 * 
 */
public class User implements Serializable {

  private static final long serialVersionUID = -4720272635999322625L;

  private String username;
  private String password;
  private String fullname;
  private String email;

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username
   *          the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password
   *          the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @return the fullname
   */
  public String getFullname() {
    return fullname;
  }

  /**
   * @param fullname
   *          the fullname to set
   */
  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email
   *          the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }

}
