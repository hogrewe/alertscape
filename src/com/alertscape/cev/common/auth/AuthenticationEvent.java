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

  private static class AuthEventType
  {
    public static final AuthEventType LOGIN = new AuthEventType("LOGIN");
    public static final AuthEventType LOGOUT = new AuthEventType("LOGOUT");
    public static final AuthEventType FAILED_LOGIN = new AuthEventType(
        "FAILED_LOGIN");

    private String type;

    private AuthEventType(String type)
    {
      this.type = type;
    }

    public String toString( )
    {
      return type;
    }
  }
}
