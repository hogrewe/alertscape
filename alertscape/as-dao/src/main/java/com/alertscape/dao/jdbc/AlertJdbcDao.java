/**
 * 
 */
package com.alertscape.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.AlertSourceRepository;
import com.alertscape.common.model.AlertStatus;
import com.alertscape.common.model.equator.AlertEquator;
import com.alertscape.common.model.equator.AttributeEquator;
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
  private static final String GET_CATEGORIES_SQL = "select def.name, cat.value from alert_categories cat, category_def def where source_id=? and alertid=? and cat.category_sid=def.sid";
  private static final String GET_LABELS_SQL = "select name, value from alert_labels where source_id=? and alertid=?";  
  private static final String INSERT_CATEGORY_SQL="insert into alert_categories (alertid,source_id,category_sid,value) VALUES (?,?,(Select sid from category_def where name=?),?)";
  private static final String UPDATE_CATEGORY_SQL="update alert_categories set value=? where source_id=? and alertid=? and category_sid=(Select sid from category_def where name=?)";  
  private static final String DELETE_CATEGORIES_ALERT_SQL="delete from alert_categories where alertid=? and source_id=?";
  private static final String INSERT_LABEL_SQL="insert into alert_labels (alertid,source_id,name,value) VALUES (?,?,?,?)";
  private static final String UPDATE_LABEL_SQL="update alert_labels set value=? where alertid=? and source_id=? and name=?";
  private static final String DELETE_LABELS_ALERT_SQL="delete from alert_labels where alertid=? and source_id=?";
  
  
  private RowMapper alertMapper = new AlertMapper();
  private AlertSourceRepository alertSourceRepository;
  public Map<String, String> attributeToColumnMap = new HashMap<String, String>();

  public AlertJdbcDao() {
    // TODO: blech, this is ugly
    attributeToColumnMap.put("acknowledgedBy", "acknowledged_by");
    attributeToColumnMap.put("item", "item");
    attributeToColumnMap.put("itemType", "item_type");
    attributeToColumnMap.put("itemManager", "item_manager");
    attributeToColumnMap.put("itemManagerType", "item_manager_type");
    attributeToColumnMap.put("longDescription", "long_description");
    attributeToColumnMap.put("shortDescription", "short_description");
    attributeToColumnMap.put("type", "type");
  }

  public void delete(final AlertSource source, final long alertId) throws DaoException {

  	deleteLabels(source, alertId);
  	deleteCategories(source, alertId);  	
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
  public Alert get(Alert a, AlertEquator equator) {
    if (equator == null) {
      return null;
    }

    StringBuilder builder = new StringBuilder("select * from alerts where source_id=?");
    List<Object> args = new ArrayList<Object>();
    
    args.add(a.getSource().getSourceId());

    for (AttributeEquator eq : equator.getAttributeEquators()) {
      String attributeName = eq.getAttributeName();
      String fieldName = attributeToColumnMap.get(attributeName);
      if (fieldName != null) {
        builder.append(" and ");
        builder.append(fieldName + "=?");
        args.add(eq.getValue(a));
      } else {
        LOG.error("Attribute passed in for filtering on alert, but not mapped to a column: " + attributeName);
      }
    }
    List<Alert> alerts = getJdbcTemplate().query(builder.toString(), args.toArray(), alertMapper);
    if (alerts != null && !alerts.isEmpty()) {
      if (alerts.size() > 1) {
        LOG.error("Looking for a unique alert but found multiple; returning first one: " + a + ", " + equator);
      }
      return alerts.get(0);
    } else {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public List<Alert> getAllAlerts(String filter) throws DaoException {
    // XXX:Get rid of sql injection vulnerability!!!!!
    String query = GET_ALL_ALERTS_SQL;
    if (filter != null && !filter.isEmpty()) {
      query += " where " + filter;
    }
    
    List<Alert> vals = getJdbcTemplate().query(query, alertMapper);
    
    LOG.info("Returing all alerts from db: " + vals);
    
    return vals;
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
    saveCategories(alert);
    saveLabels(alert);
    getAlertSourceRepository().updateAlertIdSeq(alert.getSource(), alert.getAlertId() + 1);
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

    if (attributes == null || attributes.isEmpty()) {
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
  
  private void saveCategories(Alert alert) {
    Map<String, Object> categories = alert.getMajorTags();

    if (categories == null || categories.isEmpty()) {
      return;
    }

    StringBuilder updateBuilder = new StringBuilder(UPDATE_CATEGORY_SQL);
    StringBuilder insertBuilder = new StringBuilder(INSERT_CATEGORY_SQL);

    Object[] insertArgs = new Object[4];
    Object[] updateArgs = new Object[4];
    
    int i = 0;
    for (String attrName : categories.keySet()) 
    {      
    	// build insert arguments
    	insertArgs[0] = alert.getAlertId();
    	insertArgs[1] = alert.getSource().getSourceId();
    	insertArgs[2] = attrName;
    	insertArgs[3] = categories.get(attrName);
    	
    	// build update arguments
    	updateArgs[0] = categories.get(attrName);
    	updateArgs[1] = alert.getSource().getSourceId();
    	updateArgs[2] = alert.getAlertId();
    	updateArgs[3] = attrName;
    	
    	// try to update the categories
      int numUpdated = getJdbcTemplate().update(updateBuilder.toString(), updateArgs);

      if (numUpdated > 0) 
      {
        // the update worked
      }
      else
      {      	
      	getJdbcTemplate().update(insertBuilder.toString(), insertArgs);
      }
    }
  }
  
  private void saveLabels(Alert alert) {
    Map<String, Object> labels = alert.getMinorTags();

    if (labels == null || labels.isEmpty()) {
      return;
    }
    
    StringBuilder updateBuilder = new StringBuilder(UPDATE_LABEL_SQL);
    StringBuilder insertBuilder = new StringBuilder(INSERT_LABEL_SQL);    

    Object[] insertArgs = new Object[4];
    Object[] updateArgs = new Object[4];
    
    int i = 0;
    for (String attrName : labels.keySet()) 
    {      
    	// build insert arguments
    	insertArgs[0] = alert.getAlertId();
    	insertArgs[1] = alert.getSource().getSourceId();
    	insertArgs[2] = attrName;
    	insertArgs[3] = labels.get(attrName);
    	
    	// build update arguments
    	updateArgs[0] = labels.get(attrName);
    	updateArgs[1] = alert.getAlertId();
    	updateArgs[2] = alert.getSource().getSourceId();    	
    	updateArgs[3] = attrName;
    	
    	// try to update the categories
      int numUpdated = getJdbcTemplate().update(updateBuilder.toString(), updateArgs);

      if (numUpdated > 0) 
      {
        // the update worked
      }
      else
      {      	
      	getJdbcTemplate().update(insertBuilder.toString(), insertArgs);
      }
    }
  }  
  
  
  
  private void deleteExtendedAttributes(AlertSource source, long alertId) {
    Object[] args = new Object[2];

    int i = 0;

    args[i++] = source.getSourceId();
    args[i++] = alertId;

    getJdbcTemplate().update(DELETE_EXT_ALERT_SQL, args);
  }

  private void deleteCategories(AlertSource source, long alertId) {
    Object[] args = new Object[2];

    int i = 0;

    args[i++] = alertId;
    args[i++] = source.getSourceId();    

    getJdbcTemplate().update(DELETE_CATEGORIES_ALERT_SQL, args);
  }
  
  private void deleteLabels(AlertSource source, long alertId) {
    Object[] args = new Object[2];

    int i = 0;

    args[i++] = alertId;
    args[i++] = source.getSourceId();    

    getJdbcTemplate().update(DELETE_LABELS_ALERT_SQL, args);
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

      alert.setExtendedAttributes(getExtendedAttributes(alert));      
      alert.setMajorTags(getCategories(alert));
      alert.setMinorTags(getLabels(alert));
      
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

      Map<String, Object> attr = null;
      try {
        List<Map<String, Object>> list = getJdbcTemplate().query(GET_EXT_SQL, args, new ExtendedAttributeMapper());
        if (!list.isEmpty()) {
          attr = list.get(0);
        }
      } catch (DataAccessException e) {
        LOG.info("Couldn't get attributes", e);
      }
      return attr;
    }
    
    /**
     * @param alert
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getCategories(Alert alert) {
      Object[] args = new Object[2];
      int i = 0;

      args[i++] = alert.getSource().getSourceId();
      args[i++] = alert.getAlertId();

      Map<String, Object> attr = new HashMap<String, Object>(0);
      try {
        List<Map<String, Object>> list = getJdbcTemplate().query(GET_CATEGORIES_SQL, args, new CategoryMapper());
        if (!list.isEmpty()) 
        {
        //  attr = list.get(0);
        	for (int j=0; j < list.size(); j++)
        	{
        		Map<String, Object> nextMap = list.get(j);
            attr.putAll(nextMap);
        	}        	
        }
      } catch (DataAccessException e) {
        LOG.info("Couldn't get categories", e);
      }
      
//      LOG.info("returning categories for " + alert.getAlertId() + ": " + attr);
      
      return attr;    
    }
    
    /**
     * @param alert
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getLabels(Alert alert) {
      Object[] args = new Object[2];
      int i = 0;

      args[i++] = alert.getSource().getSourceId();
      args[i++] = alert.getAlertId();

      Map<String, Object> attr = new HashMap<String, Object>(0);
      try {
        List<Map<String, Object>> list = getJdbcTemplate().query(GET_LABELS_SQL, args, new LabelMapper());
        if (!list.isEmpty()) 
        {
      		//attr = list.get(0);
        	for (int j=0; j < list.size(); j++)
        	{
        		Map<String, Object> nextMap = list.get(j);
            attr.putAll(nextMap);
        	}
        }
      } catch (DataAccessException e) {
        LOG.info("Couldn't get labels", e);
      }
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
        String label = metaData.getColumnLabel(i + 1);
        if (label.equalsIgnoreCase("source_id") || label.equalsIgnoreCase("alertid")) {
          continue;
        }
        Object value = rs.getObject(i + 1);
        attr.put(label, value);
      }

      return attr;
    }
  }
  
  private final class CategoryMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
      
    	Map<String, Object> attr = new HashMap<String, Object>();
      
      //while (rs.next()) 
      //{               
        String name = rs.getString("NAME");
        Object value = rs.getObject("VALUE");
        attr.put(name, value);
      //}

      return attr;
    }
  }
  
  private final class LabelMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
      
    	Map<String, Object> attr = new HashMap<String, Object>();
      
      //while (rs.next()) 
      //{               
        String name = rs.getString("NAME");
        Object value = rs.getObject("VALUE");
        attr.put(name, value);
      //}

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
