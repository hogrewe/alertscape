/**
 * 
 */
package com.alertscape.wizard.client;

import java.io.Serializable;

/**
 * @author josh
 * 
 */
public class InstallWizardInfo implements Serializable {
  private static final long serialVersionUID = 6355719005213775433L;

  private String driverName;
  private String url;
  private String username;
  private String password;
  private String context;
  private String jmsPort;
  private String asHome;
  private boolean noSchemaCreation;
  
  public InstallWizardInfo() {
    
  }

  /**
   * @return the driverName
   */
  public String getDriverName() {
    return driverName;
  }

  /**
   * @param driverName
   *          the driverName to set
   */
  public void setDriverName(String driverName) {
    this.driverName = driverName;
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
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password
   *          the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @return the context
   */
  public String getContext() {
    return context;
  }

  /**
   * @param context
   *          the context to set
   */
  public void setContext(String context) {
    this.context = context;
  }

  /**
   * @return the jmsPort
   */
  public String getJmsPort() {
    return jmsPort;
  }

  /**
   * @param jmsPort
   *          the jmsPort to set
   */
  public void setJmsPort(String jmsPort) {
    this.jmsPort = jmsPort;
  }

  /**
   * @return the asHome
   */
  public String getAsHome() {
    return asHome;
  }

  /**
   * @param asHome
   *          the asHome to set
   */
  public void setAsHome(String asHome) {
    this.asHome = asHome;
  }

  /**
   * @return the noSchemaCreation
   */
  public boolean isNoSchemaCreation() {
    return noSchemaCreation;
  }

  /**
   * @param noSchemaCreation the noSchemaCreation to set
   */
  public void setNoSchemaCreation(boolean noSchemaCreation) {
    this.noSchemaCreation = noSchemaCreation;
  }
}
