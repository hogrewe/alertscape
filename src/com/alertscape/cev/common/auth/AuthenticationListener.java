/*
 * Created on Aug 12, 2006
 */
package com.alertscape.cev.common.auth;

/**
 * @author josh
 * @version $Version: $
 */
public interface AuthenticationListener
{
  public void handleAuthEvent(AuthenticationEvent e);
}
