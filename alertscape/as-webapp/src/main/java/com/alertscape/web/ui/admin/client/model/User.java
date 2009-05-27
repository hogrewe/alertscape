/**
 * 
 */
package com.alertscape.web.ui.admin.client.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author josh
 * 
 */
public class User implements Serializable {

  private static final long serialVersionUID = -4720272635999322625L;

  private long id;
  private String username;
  private String fullname;
  private String email;
  private Set<String> roles = new HashSet<String>();

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

  /**
   * @return the id
   */
  public long getId() {
    return id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * @return the roles
   */
  public Set<String> getRoles() {
    return roles;
  }

  /**
   * @param roles the roles to set
   */
  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }

}
