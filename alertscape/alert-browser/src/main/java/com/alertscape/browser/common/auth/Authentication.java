/*
 * Created on Aug 12, 2006
 */
package com.alertscape.browser.common.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author josh
 * @version $Version: $
 */
public class Authentication {
	private static Map<String, User> appUsers = new HashMap<String, User>();

	private static List<AuthenticationListener> authListeners = new ArrayList<AuthenticationListener>();

	public static User getUser(String application) {
		return appUsers.get(application);
	}

	public static User login(String application, String user, char[] password) {
		// TODO: actually login somehow
		User u = new User();
		u.setUsername(user);
		u.setEmail(user + "@alertscape.com");

		appUsers.put(application, u);

		fireLoginEvent(u);

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

	private static void fireLoginEvent(User u) {
		AuthenticationEvent e = new AuthenticationEvent(
				AuthenticationEvent.LOGIN, u);
		fireAuthenticationEvent(e);
	}

	@SuppressWarnings("unused")
	private static void fireLogoutEvent(User u) {
		AuthenticationEvent e = new AuthenticationEvent(
				AuthenticationEvent.LOGOUT, u);
		fireAuthenticationEvent(e);
	}

	@SuppressWarnings("unused")
	private static void fireFailedLoginEvent() {
		AuthenticationEvent e = new AuthenticationEvent(
				AuthenticationEvent.FAILED_LOGIN, null);
		fireAuthenticationEvent(e);
	}

	private static void fireAuthenticationEvent(AuthenticationEvent e) {
		for (int i = 0, size = authListeners.size(); i < size; i++) {
			authListeners.get(i).handleAuthEvent(e);
		}
	}
}
