/**
 * 
 */
package com.alertscape.common.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.PreparedStatementSetter;
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
  private static final String INSERT_SOURCE_SQL = "insert into alert_sources (alert_source_id, alert_source_name, "
      + "short_description) values (?,?,?)";
  private static final String UPDATE_SOURCE_SQL = "upate alert_sources set alert_source_name=?, short_description=? "
      + " where alert_source_id=?";
  private static final String UPDATE_ID_SEQ = "update alert_sources set alert_id_seq=? where alert_source_id=?";

  private Map<Integer, AlertSource> sourcesMap = new HashMap<Integer, AlertSource>();

  private AlertSourceMapper sourceMapper = new AlertSourceMapper();

  public AlertSource get(int sourceId) throws DaoException {
    AlertSource source;
    synchronized (sourcesMap) {
      source = sourcesMap.get(sourceId);
      if (source == null) {
        source = (AlertSource) getJdbcTemplate().query(GET_SOURCE_SQL, new Object[] { sourceId }, sourceMapper);
        sourcesMap.put(sourceId, source);
      }
    }
    return source;
  }

  @SuppressWarnings("unchecked")
  public List<AlertSource> getAllSources() throws DaoException {
    return getJdbcTemplate().query(GET_ALL_SOURCES_SQL, sourceMapper);
  }

  public void save(AlertSource source) throws DaoException {
    AlertSource existing = get(source.getSourceId());
    if (existing == null) {
      // TODO: SAVE NEW SOURCE
    } else {
      // TODO: UPDATE EXISTING SOURCE
    }
  }

  public void reserveAlertIdSeq(final AlertSource source, final int numToReserve) throws DaoException {
//    getJdbcTemplate().
    getJdbcTemplate().update(UPDATE_ID_SEQ, new PreparedStatementSetter() {
      public void setValues(PreparedStatement ps) throws SQLException {
        int i = 1;
        ps.setLong(i++, numToReserve);
        ps.setInt(i++, source.getSourceId());
      }
    });
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
