/**
 * 
 */
package com.alertscape.pump.onramp.db;

import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.DataSourceConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.alertscape.AlertscapeException;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.pump.onramp.file.AlertLineProcessor;

/**
 * @author josh
 * 
 */
public class ConfigurableDatabaseOnramp<ID> extends DatabaseOnramp<ID> {
  private static final ASLogger LOG = ASLogger.getLogger(ConfigurableDatabaseOnramp.class);

  private String jndiDatasource;
  private String tableName;
  private String idColumn;
  private Map<String, String> columnsToFields;
  private Map<String, String> columnsToAttributes;
  private String whereClause;
  private String driverClassName;
  private String url;
  private String username;
  private String password;
  private List<AlertLineProcessor> lineProcessors;
  private String regex;
  private String regexColumn;

  private PoolingDataSource poolingDataSource;


  public void init() throws AlertscapeException {
    DataSource dataSource = null;
    if (getJndiDatasource() != null) {
      try {
        InitialContext context = new InitialContext();
        dataSource = (DataSource) context.lookupLink(getJndiDatasource());
      } catch (NamingException e) {
        throw new AlertscapeException("Couldn't get datasource at " + getJndiDatasource());
      }
    }

    if (dataSource == null) {
      if (driverClassName == null || url == null || username == null || password == null) {
        throw new AlertscapeException("Couldn't create database onramp, missing db information");
      }
      BasicDataSource bds = new BasicDataSource();
      bds.setDriverClassName(driverClassName);
      bds.setUrl(url);
      bds.setUsername(username);
      bds.setPassword(password);

      DataSourceConnectionFactory connFactory = new DataSourceConnectionFactory(bds);

      GenericObjectPool pool = new GenericObjectPool();
      pool.setTestOnBorrow(true);

      new PoolableConnectionFactory(connFactory, pool, null, "select 1 from dual", false, true);

      poolingDataSource = new PoolingDataSource(pool);

      dataSource = poolingDataSource;
    }

    AlertOnrampJdbcDao<ID> dao = new AlertOnrampJdbcDao<ID>();
    dao.setColumnsToFields(columnsToFields);
    dao.setColumnsToAttributes(columnsToAttributes);
    dao.setIdColumn(idColumn);
    dao.setTableName(tableName);
    dao.setWhereClause(whereClause);
    dao.setLineProcessors(lineProcessors);
    dao.setRegexColumn(regexColumn);
    dao.setDataSource(dataSource);
    
    dao.init();
    
    setOnrampDao(dao);

    LOG.debug("Initialized configurable db onramp");
    
    super.init();
  }
  
  public void shutdown() {
    // TODO: Close data source?
    super.shutdown();
  }

  /**
   * @return the jndiDatasource
   */
  public String getJndiDatasource() {
    return jndiDatasource;
  }

  /**
   * @param jndiDatasource
   *          the jndiDatasource to set
   */
  public void setJndiDatasource(String jndiDatasource) {
    this.jndiDatasource = jndiDatasource;
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
   * @return the driverClassName
   */
  public String getDriverClassName() {
    return driverClassName;
  }

  /**
   * @param driverClassName
   *          the driverClassName to set
   */
  public void setDriverClassName(String driverClassName) {
    this.driverClassName = driverClassName;
  }

  /**
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * @param url
   *          the url to set
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username
   *          the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the pasword
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param pasword
   *          the pasword to set
   */
  public void setPassword(String pasword) {
    this.password = pasword;
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

  /**
   * @return the lineProcessor
   */
  public List<AlertLineProcessor> getLineProcessors() {
    return lineProcessors;
  }

  /**
   * @param lineProcessors the lineProcessor to set
   */
  public void setLineProcessors(List<AlertLineProcessor> lineProcessors) {
    this.lineProcessors = lineProcessors;
  }

  /**
   * @return the regex
   */
  public String getRegex() {
    return regex;
  }

  /**
   * @param regex the regex to set
   */
  public void setRegex(String regex) {
    this.regex = regex;
  }

  /**
   * @return the regexColumn
   */
  public String getRegexColumn() {
    return regexColumn;
  }

  /**
   * @param regexColumn the regexColumn to set
   */
  public void setRegexColumn(String regexColumn) {
    this.regexColumn = regexColumn;
  }
}
