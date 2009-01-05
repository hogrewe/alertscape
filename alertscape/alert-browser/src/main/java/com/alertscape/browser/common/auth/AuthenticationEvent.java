/*
 * Created on Aug 12, 2006
 */
package com.alertscape.browser.common.auth;

import com.alertscape.common.model.AuthenticatedUser;

/**
 * @author josh
 * @version $Version: $
 */
public class AuthenticationEvent
{
  public static final AuthEventType LOGIN = AuthEventType.LOGIN;
  public static final AuthEventType LOGOUT = AuthEventType.LOGOUT;
  public static final AuthEventType FAILED_LOGIN = AuthEventType.FAILED_LOGIN;

  private AuthEventType type;
  private AuthenticatedUser user;

  public AuthenticationEvent(AuthEventType type, AuthenticatedUser u)
  {
    this.type = type;
    this.user = u;
  }

  public AuthEventType getType( )
  {
    return type;
  }

  public AuthenticatedUser getUser( )
  {
    return user;
  }

  public enum AuthEventType
  {
    LOGIN("LOGIN"),
    LOGOUT("LOGOUT"),
    FAILED_LOGIN("FAILED_LOGIN");

    private String type;

    private AuthEventType(String type)
    {
      this.type = type;
    }

    @Override
    public String toString( )
    {
      return type;
    }
  }
}
