/**
 * 
 */
package com.alertscape.service;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.AuthenticatedUser;

/**
 * @author josh
 * 
 */
public interface AuthenticationService {
  AuthenticatedUser authenticate(String username, char[] password) throws AlertscapeException;
}
