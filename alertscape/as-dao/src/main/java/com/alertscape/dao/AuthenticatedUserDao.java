/**
 * 
 */
package com.alertscape.dao;

import com.alertscape.common.model.AuthenticatedUser;

/**
 * @author josh
 *
 */
public interface AuthenticatedUserDao {
  AuthenticatedUser authenticate(String username, char[] password);
}
