/**
 * 
 */
package com.alertscape.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.alertscape.dao.TreeConfigurationDao;

/**
 * @author josh
 * 
 */
public class TreeConfigurationJdbcDao extends JdbcDaoSupport implements TreeConfigurationDao {

  private static final String TREE_SQL = "select * from tree_configurations where name=?";

  @SuppressWarnings("unchecked")
  @Override
  public String getTreeConfiguration() {
    List<String> configurations = getJdbcTemplate().query(TREE_SQL, new Object[] { "default" }, new RowMapper() {
      @Override
      public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("configuration");
      }
    });
    String config = null;
    if(!configurations.isEmpty()) {
      config = configurations.get(0);
    }
    return config;
  }

}
