/**
 * 
 */
package com.alertscape.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.AlertSourceType;
import com.alertscape.dao.AlertSourceDao;
import com.alertscape.dao.DaoException;

/**
 * @author josh
 * 
 */
public class AlertSourceJdbcDao extends JdbcDaoSupport implements AlertSourceDao {
  public static final String GET_ALL_SOURCES_SQL = "select s.alert_source_id, s.alert_source_name, s.configuration, "
      + " s.active, st.sid as type_sid, st.type, st.mapping_file from alert_sources s join alert_source_types st on st.sid=s.alert_source_type_sid ";
  public static final String GET_ALL_ACTIVE_SOURCES_SQL = GET_ALL_SOURCES_SQL + " where active=true ";
  public static final String GET_SOURCE_SQL = GET_ALL_SOURCES_SQL + " where alert_source_id=? ";
  public static final String UPDATE_ID_SEQ = "update alert_sources set alert_id_seq=? where alert_source_id=? "
      + "and alert_id_seq < ?";
  public static final String GET_ALERT_ID_SEQ = "select alert_id_seq from alert_sources where alert_source_id=?";

  public Map<Integer, AlertSource> sourcesMap = new HashMap<Integer, AlertSource>();

  public AlertSourceMapper sourceMapper = new AlertSourceMapper();

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

  public long getAlertIdSeq(AlertSource source) {
    return getJdbcTemplate().queryForLong(GET_ALERT_ID_SEQ, new Object[] { source.getSourceId() });
  }

  public void updateAlertIdSeq(final AlertSource source, final long alertId) {
    // getJdbcTemplate().
    getJdbcTemplate().update(UPDATE_ID_SEQ, new PreparedStatementSetter() {
      public void setValues(PreparedStatement ps) throws SQLException {
        int i = 1;
        ps.setLong(i++, alertId);
        ps.setInt(i++, source.getSourceId());
        ps.setLong(i++, alertId);
      }
    });
  }

  private static class AlertSourceMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
      int sourceId = rs.getInt("ALERT_SOURCE_ID");
      String sourceName = rs.getString("ALERT_SOURCE_NAME");
      AlertSource source = new AlertSource(sourceId, sourceName);
      source.setConfigXml(rs.getString("configuration"));
      source.setActive(rs.getBoolean("active"));
      AlertSourceType type = new AlertSourceType();
      type.setSid(rs.getLong("type_sid"));
      type.setType(rs.getString("type"));
      type.setMappingFile(rs.getString("mapping_file"));
      source.setType(type);

      return source;
    }
  }

}
