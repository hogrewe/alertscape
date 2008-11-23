/**
 * 
 */
package com.alertscape.common.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.alertscape.common.dao.AlertSourceDao;
import com.alertscape.common.dao.DaoException;
import com.alertscape.common.model.AlertSource;

/**
 * @author josh
 * 
 */
public class AlertSourceJdbcDao extends JdbcDaoSupport implements AlertSourceDao {
  private static final String GET_ALL_SOURCES_SQL = "select * from alert_sources";
  private static final String GET_SOURCE_SQL = "select * from alert_sources where alert_source_id=?";

  private AlertSourceMapper sourceMapper = new AlertSourceMapper();

  public AlertSource get(int sourceId) throws DaoException {
    return (AlertSource) getJdbcTemplate().query(GET_SOURCE_SQL, new Object[] { sourceId }, sourceMapper);
  }

  @SuppressWarnings("unchecked")
  public List<AlertSource> getAllSources() throws DaoException {
    return getJdbcTemplate().query(GET_ALL_SOURCES_SQL, sourceMapper);
  }

  private static class AlertSourceMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
      int sourceId = rs.getInt("ALERT_SOURCE_ID");
      String sourceName = rs.getString("ALERT_SOURCE_NAME");
      AlertSource source = new AlertSource(sourceId, sourceName);

      return source;
    }

  }

}
