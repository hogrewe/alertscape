/**
 * 
 */
package com.alertscape.pump.onramp.db;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.severity.Severity;
import com.alertscape.common.model.severity.SeverityFactory;

/**
 * @author josh
 * 
 */
public class AlertOnrampJdbcDao extends JdbcDaoSupport implements AlertOnrampDao {
  private static final ASLogger LOG = ASLogger.getLogger(AlertOnrampJdbcDao.class);
  private String tableName;
  private String whereClause;
  private String idColumn;
  private Map<String, String> columnsToFields;
  private Map<String, String> columnsToAttributes;
  private Map<String, Integer> severityMapping;
  private String query;
  private Map<String, Method> cachedSetters;

  @SuppressWarnings("unchecked")
  public Object getNextAlerts(int batchSize, Object lastId, List<Alert> nextAlerts) {
    if(lastId == null) {
      lastId = 0;
    }
    final Object[] lastIdHolder = new Object[1];
    lastIdHolder[0] = lastId;
    List<Alert> alerts = getJdbcTemplate().query(query, new Object[]{lastId, batchSize}, new RowMapper() {
      public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Alert alert = new Alert();
        SeverityFactory factory = SeverityFactory.getInstance();
        alert.setSeverity(factory.getSeverity(0));
        
        for (String column : columnsToFields.keySet()) {
          Object value = rs.getObject(column);
          String fieldName = columnsToFields.get(column);
          if(fieldName.equals("severity")) {
            Integer sevNum = severityMapping.get(value);
            if(sevNum == null || sevNum < 0) {
              sevNum = 0;
            } else if (sevNum >= factory.getNumSeverities()) {
              sevNum = factory.getNumSeverities()-1;
            }
            Severity severity = factory.getSeverity(sevNum);
            value = severity;
          }
          Method setter = getSetter(fieldName);
          if(setter != null) {
            try {
              setter.invoke(alert, value);
            } catch (Exception e) {
              LOG.error("Couldn't set " + fieldName + " to " + value);
            }
          }
        }
        
        for (String column : columnsToAttributes.keySet()) {
          Object value = rs.getObject(column);
          
          String attrName = columnsToAttributes.get(column);
          
          alert.getExtendedAttributes().put(attrName, value); 
        }
        
        lastIdHolder[0] = rs.getObject(idColumn);
        return alert;
      }
    });
    nextAlerts.addAll(alerts);
    return lastIdHolder[0];
  }

  public void init() {
    generateQuery();
    cachedSetters = new HashMap<String, Method>();
    if(columnsToAttributes == null) {
      columnsToAttributes = Collections.emptyMap();
    }
    
    if(columnsToFields == null) {
      LOG.error("No field mappings defined");
    }
  }

  /**
   * @return the tableName
   */
  public String getTableName() {
    return tableName;
  }

  /**
   * @param tableName
   *          the tableName to set
   */
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  /**
   * @return the columnsToFields
   */
  public Map<String, String> getColumnsToFields() {
    return columnsToFields;
  }

  /**
   * @param columnsToFields
   *          the columnsToFields to set
   */
  public void setColumnsToFields(Map<String, String> columnsToFields) {
    this.columnsToFields = columnsToFields;
  }

  /**
   * @return the columnsToAttributes
   */
  public Map<String, String> getColumnsToAttributes() {
    return columnsToAttributes;
  }

  /**
   * @param columnsToAttributes
   *          the columnsToAttributes to set
   */
  public void setColumnsToAttributes(Map<String, String> columnsToAttributes) {
    this.columnsToAttributes = columnsToAttributes;
  }

  /**
   * @return the whereClause
   */
  public String getWhereClause() {
    return whereClause;
  }

  /**
   * @param whereClause
   *          the whereClause to set
   */
  public void setWhereClause(String whereClause) {
    this.whereClause = whereClause;
  }

  private void generateQuery() {
    query = "select * from " + tableName + " where " + (whereClause == null ? "" : whereClause + " and ") + idColumn
        + ">? order by " + idColumn + " limit ?";
    
    LOG.info("Generated query: " + query);
  }

  /**
   * @return the idColumn
   */
  public String getIdColumn() {
    return idColumn;
  }

  /**
   * @param idColumn
   *          the idColumn to set
   */
  public void setIdColumn(String idColumn) {
    this.idColumn = idColumn;
  }
  
  protected Method getSetter(String fieldName) {
    Method setter = cachedSetters.get(fieldName);
    if(setter != null) {
      return setter;
    }
    
    try {
      PropertyDescriptor d = new PropertyDescriptor(fieldName, Alert.class);
      setter = d.getWriteMethod();
      cachedSetters.put(fieldName, setter);
    } catch (IntrospectionException e) {
      LOG.error("Couldn't make setter for " + fieldName, e);
    }
    return setter;
  }

  /**
   * @return the severityMapping
   */
  public Map<String, Integer> getSeverityMapping() {
    return severityMapping;
  }

  /**
   * @param severityMapping the severityMapping to set
   */
  public void setSeverityMapping(Map<String, Integer> severityMapping) {
    this.severityMapping = severityMapping;
  }
}