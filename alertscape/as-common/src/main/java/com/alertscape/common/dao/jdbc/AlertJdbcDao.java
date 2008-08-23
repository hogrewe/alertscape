/**
 * 
 */
package com.alertscape.common.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.alertscape.common.dao.AlertDao;
import com.alertscape.common.dao.DaoException;
import com.alertscape.common.model.Alert;
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
  private static final String INSERT_ALERT_SQL = null;
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

  public void save(Alert alert) throws DaoException {
    if (alert.getAlertId() > 0) {
      int updated = update(alert);
      if (updated < 1) {
        throw new DaoException("No existing alert found with id: " + alert.getAlertId());
      }
    } else {
      insert(alert);
    }
  }

  /**
   * @param alert
   */
  private void insert(Alert alert) {
    PreparedStatementSetter pss = new PreparedStatementSetter() {
      public void setValues(PreparedStatement ps) throws SQLException {
      }
    };
    getJdbcTemplate().update(INSERT_ALERT_SQL, pss);
    // TODO Auto-generated method stub

  }

  /**
   * @param alert
   * @return
   */
  private int update(Alert alert) {
    // TODO Auto-generated method stub
    return 0;
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
