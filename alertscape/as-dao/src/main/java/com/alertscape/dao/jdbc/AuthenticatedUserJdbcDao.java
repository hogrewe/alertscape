/**
 * 
 */
package com.alertscape.dao.jdbc;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
  private static final String GET_ALL_USERS_SQL = "select * from as_user where active=true";
  private static final String GET_USER_SQL = "select * from as_user where user_id=?";
  private static final String INSERT_USER_SQL = "insert into as_user (username, password, email, fullname) values (?,?,?,?)";
  private static final String UPDATE_USER_SQL = "update as_user set username=?, password=?, email=?, fullname=? where user_id=?";
  private static final String UPDATE_USER_NO_PASSWORD_SQL = "update as_user set username=?, email=?, fullname=? where user_id=?";
  private static final String GET_ROLES_SQL = "select role.name as role_name from as_user_role aur join as_role role on role.sid=aur.as_role_sid where aur.as_user_id=?";

  public AuthenticatedUser authenticate(String username, char[] password) {
    Object[] args = new Object[2];
    args[0] = username;
    args[1] = hashPassword(password);

    AuthenticatedUser user = (AuthenticatedUser) getJdbcTemplate().queryForObject(AUTHENTICATE_USER_SQL, args,
        new UserMapper());

    return user;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<AuthenticatedUser> getAll() {
    return getJdbcTemplate().query(GET_ALL_USERS_SQL, new UserMapper());
  }

  @SuppressWarnings("unchecked")
  public AuthenticatedUser get(long id) {
    List<AuthenticatedUser> users = getJdbcTemplate().query(GET_USER_SQL, new Object[] { id }, new UserMapper());
    if (users != null && !users.isEmpty()) {
      return users.get(0);
    } else {
      return null;
    }
  }

  @Override
  public void save(AuthenticatedUser user, char[] password) {
    List<Object> args = new ArrayList<Object>();
    args.add(user.getUsername());
    if (password != null) {
      args.add(hashPassword(password));
    }
    args.add(user.getEmail());
    args.add(user.getFullName());

    AuthenticatedUser existing = get(user.getUserId());
    if (existing != null) {
      args.add(user.getUserId());
      if (password != null) {
        getJdbcTemplate().update(UPDATE_USER_SQL, args.toArray());
      } else {
        getJdbcTemplate().update(UPDATE_USER_NO_PASSWORD_SQL, args.toArray());
      }
    } else {
      if (password == null) {
        throw new IllegalArgumentException("Cannot create a user with a null password");
      }
      getJdbcTemplate().update(INSERT_USER_SQL, args.toArray());
    }
  }
  
  @SuppressWarnings("unchecked")
  private Set<String> getRoles(AuthenticatedUser user) {
    List<String> roles = getJdbcTemplate().query(GET_ROLES_SQL, new Object[] { user.getUserId() }, new RowMapper() {

      @Override
      public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("role_name");
      }
      
    });
    
    return new HashSet<String>(roles);
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
      
      Set<String> roles = getRoles(user);
      user.setRoles(roles);

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
