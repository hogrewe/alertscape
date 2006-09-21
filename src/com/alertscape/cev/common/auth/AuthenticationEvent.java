/*
 * Created on Aug 12, 2006
 */
package com.alertscape.cev.common.auth;

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
  private User user;

  public AuthenticationEvent(AuthEventType type, User user)
  {
    this.type = type;
    this.user = user;
  }

  public AuthEventType getType( )
  {
    return type;
  }

  public User getUser( )
  {
    return user;
  }

  private enum AuthEventType
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
