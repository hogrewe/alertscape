/*
 * Created on Aug 12, 2006
 */
package com.alertscape.browser.common.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alertscape.AlertscapeException;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.AuthenticatedUser;
import com.alertscape.service.AuthenticationService;

/**
 * @author josh
 * @version $Version: $
 */
public class Authentication {
  private static final ASLogger LOG = ASLogger.getLogger(Authentication.class);
  private static Map<String, AuthenticatedUser> appUsers = new HashMap<String, AuthenticatedUser>();

  private static List<AuthenticationListener> authListeners = new ArrayList<AuthenticationListener>();
  private static AuthenticationService authenticationService;

  public static AuthenticatedUser getUser(String application) {
    return appUsers.get(application);
  }

  public static AuthenticatedUser login(String application, String user, char[] password) {
    AuthenticatedUser u = null;

    try {
      u = authenticationService.authenticate(user, password);
    } catch (Exception e) {
      LOG.error("Problem logging in", e);
    }

    if (u == null) {
      LOG.info("Failed login attempt");
      fireFailedLoginEvent();
    } else {
      appUsers.put(application, u);

      fireLoginEvent(u);
    }

    return u;
  }

  public static void addAuthenticationListener(AuthenticationListener l) {
    if (!authListeners.contains(l)) {
      authListeners.add(l);
    }
  }

  public static void removeAuthenticationListener(AuthenticationListener l) {
    authListeners.remove(l);
  }

  private static void fireLoginEvent(AuthenticatedUser u) {
    AuthenticationEvent e = new AuthenticationEvent(AuthenticationEvent.LOGIN, u);
    fireAuthenticationEvent(e);
  }

  @SuppressWarnings("unused")
  private static void fireLogoutEvent(AuthenticatedUser u) {
    AuthenticationEvent e = new AuthenticationEvent(AuthenticationEvent.LOGOUT, u);
    fireAuthenticationEvent(e);
  }

  private static void fireFailedLoginEvent() {
    AuthenticationEvent e = new AuthenticationEvent(AuthenticationEvent.FAILED_LOGIN, null);
    fireAuthenticationEvent(e);
  }

  private static void fireAuthenticationEvent(AuthenticationEvent e) {
    for (int i = 0, size = authListeners.size(); i < size; i++) {
      authListeners.get(i).handleAuthEvent(e);
    }
  }

  /**
   * @param authenticationService
   *          the authenticationService to set
   */
  public static void setAuthenticationService(AuthenticationService authenticationService) {
    Authentication.authenticationService = authenticationService;
  }
}
