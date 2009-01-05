/**
 * 
 */
package com.alertscape.dao.jdbc;

import junit.framework.TestCase;

/**
 * @author josh
 *
 */
public class AuthenticatedUserJdbcDaoTest extends TestCase {

  /**
   * Test method for {@link com.alertscape.dao.jdbc.AuthenticatedUserJdbcDao#hashPassword(java.lang.String)}.
   */
  public void testHashPassword() {
    AuthenticatedUserJdbcDao user = new AuthenticatedUserJdbcDao();
    System.out.println(user.hashPassword("blah".toCharArray()));
  }

}
