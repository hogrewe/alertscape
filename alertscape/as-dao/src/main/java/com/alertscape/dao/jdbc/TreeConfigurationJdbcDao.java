/**
 * 
 */
package com.alertscape.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
  private static final String INSERT_TREE_SQL = "insert into tree_configurations (configuration, name) values (?,?)";
  private static final String UPDATE_TREE_SQL = "update tree_configurations set configuration=? where name=?";

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
    if (!configurations.isEmpty()) {
      config = configurations.get(0);
    }
    return config;
  }

  @Override
  public void save(String configuration) {
    String existing = getTreeConfiguration();
    List<Object> args = new ArrayList<Object>();
    args.add(configuration);
    args.add("default");

    if (existing == null) {
      getJdbcTemplate().update(INSERT_TREE_SQL, args.toArray());
    } else {
      getJdbcTemplate().update(UPDATE_TREE_SQL, args.toArray());
    }
  }

}
