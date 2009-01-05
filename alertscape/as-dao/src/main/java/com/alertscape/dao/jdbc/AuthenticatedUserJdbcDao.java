/**
 * 
 */
package com.alertscape.dao.jdbc;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.alertscape.common.model.AuthenticatedUser;
import com.alertscape.dao.AuthenticatedUserDao;

/**
 * @author josh
 * 
 */
public class AuthenticatedUserJdbcDao extends JdbcDaoSupport implements AuthenticatedUserDao {
  private static final String AUTHENTICATE_USER_SQL = "select * from as_user where username=? and upper(password)=?";

  public AuthenticatedUser authenticate(String username, char[] password) {
    Object[] args = new Object[2];
    args[0] = username;
    args[1] = hashPassword(password);    

    AuthenticatedUser user = (AuthenticatedUser) getJdbcTemplate().queryForObject(AUTHENTICATE_USER_SQL, args,
        new UserMapper());

    return user;
  }

  /**
   * @author josh
   * 
   */
  private final class UserMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
      AuthenticatedUser user = new AuthenticatedUser();

      user.setEmail(rs.getString("EMAIL"));
      user.setFullName(rs.getString("FULLNAME"));
      user.setUserId(rs.getInt("USER_ID"));
      user.setUsername(rs.getString("USERNAME"));

      return user;
    }
  }
  
  protected String hashPassword(char[] password) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA");
    } catch (NoSuchAlgorithmException e) {
      return null;
    }

    byte[] b = String.valueOf(password).getBytes();
    byte[] digest = md.digest(b);
    
    BigInteger bi = new BigInteger(digest);
    
    return bi.toString(16).toUpperCase();
  }

}
