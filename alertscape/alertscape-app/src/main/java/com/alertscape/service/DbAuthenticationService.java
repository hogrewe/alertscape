/**
 * 
 */
package com.alertscape.service;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.AuthenticatedUser;
import com.alertscape.dao.AuthenticatedUserDao;

/**
 * @author josh
 *
 */
public class DbAuthenticationService implements AuthenticationService {
  private AuthenticatedUserDao userDao;

  public AuthenticatedUser authenticate(String username, char[] password) throws AlertscapeException {
    AuthenticatedUser user = userDao.authenticate(username, password);
    
    return user;
  }

  /**
   * @return the userDao
   */
  public AuthenticatedUserDao getUserDao() {
    return userDao;
  }

  /**
   * @param userDao the userDao to set
   */
  public void setUserDao(AuthenticatedUserDao userDao) {
    this.userDao = userDao;
  }

}
