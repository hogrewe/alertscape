/*
 * Created on Aug 12, 2006
 */
package com.alertscape.browser.common.auth;

/**
 * @author josh
 * @version $Version: $
 */
public class User
{
  private String username;
  private String fullname;
  private String email;
  private String sessionKey;

  public String getSessionKey( )
  {
    return sessionKey;
  }

  protected void setSessionKey(String sessionKey)
  {
    this.sessionKey = sessionKey;
  }

  public String getEmail( )
  {
    return email;
  }

  protected void setEmail(String email)
  {
    this.email = email;
  }

  public String getFullname( )
  {
    return fullname;
  }

  protected void setFullname(String fullname)
  {
    this.fullname = fullname;
  }

  public String getUsername( )
  {
    return username;
  }

  protected void setUsername(String username)
  {
    this.username = username;
  }
}
