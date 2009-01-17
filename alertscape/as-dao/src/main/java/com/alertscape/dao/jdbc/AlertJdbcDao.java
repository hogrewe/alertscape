/**
 * 
 */
package com.alertscape.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.AlertSourceRepository;
import com.alertscape.common.model.Alert.AlertStatus;
import com.alertscape.common.model.severity.Severity;
import com.alertscape.common.model.severity.SeverityFactory;
import com.alertscape.dao.AlertDao;
import com.alertscape.dao.DaoException;

/**
 * @author josh
 * 
 */
public class AlertJdbcDao extends JdbcDaoSupport implements AlertDao {

  private static final String DELETE_EXT_ALERT_SQL = "delete from ext_alert_attributes where alert_source_id=? and alert_id=?";
  private static final String DELETE_ALERT_SQL = "delete from alerts where source_id=? and alertid=?";
  private static final String GET_ALERT_SQL = "select * from alerts a "
      + " left outer join ext_alert_attributes attr on attr.alert_source_id=a.source_id and attr.alert_id=a.alertid "
      + " where a.source_id=? and a.alertid=?";
  private static final String GET_ALL_ALERTS_SQL = "select * from alerts a "
      + " left outer join ext_alert_attributes attr on attr.alert_source_id=a.source_id and attr.alert_id=a.alertid ";
  private static final String GET_ALERTS_FOR_SOURCE_SQL = "select * from alerts a "
      + " join alert_sources src on src.alert_source_id=a.source_id "
      + " left outer join ext_alert_attributes attr on attr.alert_source_id=a.source_id and attr.alert_id=a.alertid "
      + " where src.alert_source_name=?";
  private static final String INSERT_ALERT_SQL = "insert into alerts "
      + "(alertid, short_description, long_description, severity, count, source_id, first_occurence, last_occurence,"
      + "item, item_type, item_manager, item_manager_type, type, acknowledged_by) "
      + "values (?,?,?,?,?,(select alert_source_id from alert_sources where alert_source_name=?),?,?,?,?,?,?,?,?)";
  private static final String INSERT_EXT_SQL_PRE = "insert into ext_alert_attributes "
    + "(";
  private static final String UPDATE_ALERT_SQL = "update alerts set short_description=?, long_description=?, "
      + "severity=?, count=?, last_occurence=?, acknowledged_by=? where source_id=? and alertid=?";

  private RowMapper alertMapper = new AlertMapper();
  private AlertSourceRepository alertSourceRepository;

  public void delete(final AlertSource source, final long alertId) throws DaoException {
    PreparedStatementSetter pss = new PreparedStatementSetter() {
      public void setValues(PreparedStatement ps) throws SQLException {
        int i = 1;
        ps.setLong(i++, source.getSourceId());
        ps.setLong(i++, alertId);
      }
    };
    getJdbcTemplate().update(DELETE_ALERT_SQL, pss);
  }

  @SuppressWarnings("unchecked")
  public Alert get(final AlertSource source, final long alertId) throws DaoException {
    PreparedStatementSetter pss = new PreparedStatementSetter() {

      public void setValues(PreparedStatement ps) throws SQLException {
        int i = 1;
        ps.setLong(i++, source.getSourceId());
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
    List<Alert> alerts = getJdbcTemplate().query(GET_ALERTS_FOR_SOURCE_SQL, new Object[] { source.getSourceName() },
        alertMapper);

    return alerts;
  }

  public void save(Alert alert) throws DaoException {
    Alert existing = get(alert.getSource(), alert.getAlertId());
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
        Severity severity = alert.getSeverity();
        ps.setInt(i++, severity == null ? 0 : severity.getLevel());
        ps.setLong(i++, alert.getCount());
        ps.setString(i++, alert.getSource().getSourceName());
        Timestamp firstOccur = new Timestamp(alert.getFirstOccurence().getTime());
        ps.setTimestamp(i++, firstOccur);
        Timestamp lastOccur = new Timestamp(alert.getLastOccurence().getTime());
        ps.setTimestamp(i++, lastOccur);
        ps.setString(i++, alert.getItem());
        ps.setString(i++, alert.getItemType());
        ps.setString(i++, alert.getItemManager());
        ps.setString(i++, alert.getItemManagerType());
        ps.setString(i++, alert.getType());
        ps.setString(i++, alert.getAcknowledgedBy());
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
        ps.setString(i++, alert.getAcknowledgedBy());
        ps.setLong(i++, alert.getSource().getSourceId());
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

      // TODO: look at taking these out
      alert.setItem(rs.getString("item"));
      alert.setItemType(rs.getString("item_type"));
      alert.setItemManager(rs.getString("item_manager"));
      alert.setItemManagerType(rs.getString("item_manager_type"));
      alert.setType(rs.getString("type"));
      alert.setAcknowledgedBy(rs.getString("acknowledged_by"));

      alert.setSource(alertSourceRepository.getAlertSource(rs.getInt("source_id")));

      alert.setStatus(AlertStatus.STANDING);
      return alert;
    }
  }

  /**
   * @return the alertSourceRepository
   */
  public AlertSourceRepository getAlertSourceRepository() {
    return alertSourceRepository;
  }

  /**
   * @param alertSourceRepository
   *          the alertSourceRepository to set
   */
  public void setAlertSourceRepository(AlertSourceRepository alertSourceRepository) {
    this.alertSourceRepository = alertSourceRepository;
  }

}
