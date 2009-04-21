/**
 * 
 */
package com.alertscape.dao;

import java.util.List;

import com.alertscape.common.model.AuthenticatedUser;

/**
 * @author josh
 *
 */
public interface AuthenticatedUserDao {
  AuthenticatedUser authenticate(String username, char[] password);
  List<AuthenticatedUser> getAll();
  void save(AuthenticatedUser user, char[] password);
}
