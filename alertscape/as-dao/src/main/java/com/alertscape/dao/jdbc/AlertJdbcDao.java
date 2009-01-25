/**
 * 
 */
package com.alertscape.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.alertscape.common.logging.ASLogger;
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
  private static final ASLogger LOG = ASLogger.getLogger(AlertJdbcDao.class);

  private static final String DELETE_ALERT_SQL = "delete from alerts where source_id=? and alertid=?";
  private static final String GET_ALERT_SQL = "select * from alerts a  where a.source_id=? and a.alertid=?";
  private static final String GET_ALL_ALERTS_SQL = "select * from alerts a ";
  private static final String GET_ALERTS_FOR_SOURCE_SQL = "select * from alerts a "
      + " join alert_sources src on src.alert_source_id=a.source_id " + " where src.alert_source_name=?";
  private static final String INSERT_ALERT_SQL = "insert into alerts "
      + "(alertid, short_description, long_description, severity, count, source_id, first_occurence, last_occurence,"
      + "item, item_type, item_manager, item_manager_type, type, acknowledged_by) "
      + "values (?,?,?,?,?,(select alert_source_id from alert_sources where alert_source_name=?),?,?,?,?,?,?,?,?)";
  private static final String UPDATE_ALERT_SQL = "update alerts set short_description=?, long_description=?, "
      + "severity=?, count=?, last_occurence=?, acknowledged_by=? where source_id=? and alertid=?";
  private static final String DELETE_EXT_ALERT_SQL = "delete from ext_alert_attributes where source_id=? and alertid=?";
  private static final String INSERT_EXT_SQL_PRE = "insert into ext_alert_attributes (";
  private static final String UPDATE_EXT_SQL_PRE = "update ext_alert_attributes set ";
  private static final String GET_EXT_SQL = "select * from ext_alert_attributes where source_id=? and alertid=?";

  private RowMapper alertMapper = new AlertMapper();
  private AlertSourceRepository alertSourceRepository;

  public void delete(final AlertSource source, final long alertId) throws DaoException {

    deleteExtendedAttributes(source, alertId);
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
    saveExtendedAttributes(alert);
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

  private void saveExtendedAttributes(Alert alert) {
    Map<String, Object> attributes = alert.getExtendedAttributes();
    
    if(attributes.isEmpty()) {
      return;
    }

    StringBuilder updateBuilder = new StringBuilder(UPDATE_EXT_SQL_PRE);
    StringBuilder insertBuilder = new StringBuilder(INSERT_EXT_SQL_PRE);
    StringBuilder insertValuesBuilder = new StringBuilder("(?,?,");

    Object[] args = new Object[attributes.size() + 2];
    int i = 0;
    boolean first = true;
    for (String attrName : attributes.keySet()) {
      if (!first) {
        updateBuilder.append(", ");
        insertBuilder.append(", ");
        insertValuesBuilder.append(", ");
      }
      first = false;
      updateBuilder.append(attrName + "=? ");
      args[i++] = attributes.get(attrName);
      insertBuilder.append(attrName);
      insertValuesBuilder.append("?");
    }

    args[i++] = alert.getSource().getSourceId();
    args[i++] = alert.getAlertId();

    updateBuilder.append(" where source_id=? and alertid=?");
    insertBuilder.append(", source_id, alertid) values ");
    insertValuesBuilder.append(")");

    int numUpdated = getJdbcTemplate().update(updateBuilder.toString(), args);

    if (numUpdated > 0) {
      return;
    }

    String insert = insertBuilder.toString() + insertValuesBuilder.toString();
    getJdbcTemplate().update(insert, args);

  }

  private void deleteExtendedAttributes(AlertSource source, long alertId) {
    Object[] args = new Object[2];

    int i = 0;

    args[i++] = source.getSourceId();
    args[i++] = alertId;

    getJdbcTemplate().update(DELETE_EXT_ALERT_SQL, args);
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

      try {
        alert.setExtendedAttributes(getExtendedAttributes(alert));
      } catch (Exception e) {
        // This shouldn't happen, but it's no harm if it does
      }
      return alert;
    }

    /**
     * @param alert
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getExtendedAttributes(Alert alert) {
      Object[] args = new Object[2];
      int i = 0;

      args[i++] = alert.getSource().getSourceId();
      args[i++] = alert.getAlertId();

      Map<String, Object> attr = (Map<String, Object>) getJdbcTemplate().queryForObject(GET_EXT_SQL, args,
          new ExtendedAttributeMapper());
      return attr;
    }
  }

  /**
   * @author josh
   * 
   */
  private final class ExtendedAttributeMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
      Map<String, Object> attr = new HashMap<String, Object>();
      ResultSetMetaData metaData = rs.getMetaData();
      for (int i = 0; i < metaData.getColumnCount(); i++) {
        String label = metaData.getColumnLabel(i);
        if (label.equalsIgnoreCase("source_id") || label.equalsIgnoreCase("alertid")) {
          continue;
        }
        Object value = rs.getObject(i);
        attr.put(label, value);
      }

      return attr;
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
