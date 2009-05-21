/**
 * 
 */
package com.alertscape.web.ui.auth;

import java.util.regex.Pattern;

/**
 * @author josh
 * 
 */
public class AuthorizedUrl {
  private String url;
  private String role;
  private Pattern pattern;

  /**
   * @param url
   * @param role
   */
  public AuthorizedUrl(String url, String role) {
    setUrl(url);
    setRole(role);
  }

  /**
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * @param url
   *          the url to set
   */
  public void setUrl(String url) {
    this.url = url;
    if (url == null) {
      pattern = null;
    } else {
      pattern = Pattern.compile(url);
    }
  }

  /**
   * @return the role
   */
  public String getRole() {
    return role;
  }

  /**
   * @param role
   *          the role to set
   */
  public void setRole(String role) {
    this.role = role;
  }

  public boolean matches(String url) {
    if (pattern != null) {
      return pattern.matcher(url).matches();
    } else {
      return false;
    }
  }
}
