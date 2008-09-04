/**
 * 
 */
package com.alertscape.common.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.alertscape.common.dao.AlertDao;
import com.alertscape.common.dao.DaoException;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.Alert.AlertStatus;
import com.alertscape.common.model.severity.SeverityFactory;

/**
 * @author josh
 * 
 */
public class AlertJdbcDao extends JdbcDaoSupport implements AlertDao {

  private static final String DELETE_ALERT_SQL = "delete from alerts where alertid=?";
  private static final String GET_ALERT_SQL = "select * from alerts where alertid=?";
  private static final String GET_ALL_ALERTS_SQL = "select * from alerts";
  private static final String GET_ALERTS_FOR_SOURCE_SQL = "select * from alerts where alert_source_id="
      + "(select alert_source_id from alert_sources where alert_source_name=?)";
  private static final String INSERT_ALERT_SQL = "insert into alerts "
      + "(alertid, short_description, long_description, severity, count, source_id, first_occurence, last_occurence) "
      + "values (?,?,?,?,?,(select alert_source_id from alert_sources where alert_source_name=?),?,?)";
  private static final String UPDATE_ALERT_SQL = "update alerts set short_description=?, long_description=?, "
      + "severity=?, count=?, last_occurence=? where alertid=?";

  private RowMapper alertMapper = new AlertMapper();

  public void delete(final long alertId) throws DaoException {
    PreparedStatementSetter pss = new PreparedStatementSetter() {
      public void setValues(PreparedStatement ps) throws SQLException {
        int i = 1;
        ps.setLong(i++, alertId);
      }
    };
    getJdbcTemplate().update(DELETE_ALERT_SQL, pss);
  }

  @SuppressWarnings("unchecked")
  public Alert get(final long alertId) throws DaoException {
    PreparedStatementSetter pss = new PreparedStatementSetter() {

      public void setValues(PreparedStatement ps) throws SQLException {
        int i = 1;
        ps.setLong(i++, alertId);
      }
    };
    List<Alert> alerts = getJdbcTemplate().query(GET_ALERT_SQL, pss, alertMapper);
    if (alerts != null && !alerts.isEmpty()) {
      return alerts.get(0);
    } else {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public List<Alert> getAllAlerts() throws DaoException {
    return getJdbcTemplate().query(GET_ALL_ALERTS_SQL, alertMapper);
  }

  @SuppressWarnings("unchecked")
  public List<Alert> getAlertsForSource(AlertSource source) throws DaoException {
    return getJdbcTemplate().query(GET_ALERTS_FOR_SOURCE_SQL, new Object[] { source.getSourceName() }, alertMapper);
  }

  public void save(Alert alert) throws DaoException {
    Alert existing = get(alert.getAlertId());
    if (existing != null) {
      update(alert);
    } else {
      insert(alert);
    }
  }

  /**
   * @param alert
   */
  private void insert(final Alert alert) {
    PreparedStatementSetter pss = new PreparedStatementSetter() {
      public void setValues(PreparedStatement ps) throws SQLException {
        int i = 1;
        ps.setLong(i++, alert.getAlertId());
        ps.setString(i++, alert.getShortDescription());
        ps.setString(i++, alert.getLongDescription());
        ps.setInt(i++, alert.getSeverity().getLevel());
        ps.setLong(i++, alert.getCount());
        ps.setString(i++, alert.getSource().getSourceName());
        Timestamp firstOccur = new Timestamp(alert.getFirstOccurence().getTime());
        ps.setTimestamp(i++, firstOccur);
        Timestamp lastOccur = new Timestamp(alert.getLastOccurence().getTime());
        ps.setTimestamp(i++, lastOccur);
      }
    };
    getJdbcTemplate().update(INSERT_ALERT_SQL, pss);
  }

  /**
   * @param alert
   * @return
   */
  private int update(final Alert alert) {
    PreparedStatementSetter pss = new PreparedStatementSetter() {
      public void setValues(PreparedStatement ps) throws SQLException {
        int i = 1;
        ps.setString(i++, alert.getShortDescription());
        ps.setString(i++, alert.getLongDescription());
        ps.setInt(i++, alert.getSeverity().getLevel());
        ps.setLong(i++, alert.getCount());
        Timestamp lastOccur = new Timestamp(alert.getLastOccurence().getTime());
        ps.setTimestamp(i++, lastOccur);
        ps.setLong(i++, alert.getAlertId());
      }
    };
    return getJdbcTemplate().update(UPDATE_ALERT_SQL, pss);
  }

  private final class AlertMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
      SeverityFactory severityFactory = SeverityFactory.getInstance();

      Alert alert = new Alert();
      alert.setAlertId(rs.getLong("alertid"));
      alert.setCount(rs.getLong("count"));
      alert.setFirstOccurence(rs.getTimestamp("first_occurence"));
      alert.setLastOccurence(rs.getTimestamp("last_occurence"));
      alert.setShortDescription(rs.getString("short_description"));
      alert.setLongDescription(rs.getString("long_description"));
      alert.setSeverity(severityFactory.getSeverity(rs.getInt("severity")));
      // XXX: Need to get the source somehow
      alert.setSource(null);
      alert.setStatus(AlertStatus.STANDING);
      return alert;
    }
  }

}
