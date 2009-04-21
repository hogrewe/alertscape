/**
 * 
 */
package com.alertscape.web.service;

import java.util.List;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.AlertAttributeDefinition;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.AuthenticatedUser;
import com.alertscape.dao.AlertAttributeDefinitionDao;
import com.alertscape.dao.AlertSourceDao;
import com.alertscape.dao.AuthenticatedUserDao;
import com.alertscape.dao.TreeConfigurationDao;

/**
 * @author josh
 * 
 */
public class AdminServiceImpl implements AdminService {
  private static final ASLogger LOG = ASLogger.getLogger(AdminServiceImpl.class);
  private AlertSourceDao alertSourceDao;
  private AuthenticatedUserDao userDao;
  private TreeConfigurationDao treeConfigurationDao;
  private AlertAttributeDefinitionDao attributeDefinitionDao;

  /* (non-Javadoc)
   * @see com.alertscape.web.service.AdminService#getUsers()
   */
  public List<AuthenticatedUser> getUsers() {
    return getUserDao().getAll();
  }

  /* (non-Javadoc)
   * @see com.alertscape.web.service.AdminService#getOnramps()
   */
  public List<AlertSource> getOnramps() {
    return getAlertSourceDao().getAllSources();
  }

  /* (non-Javadoc)
   * @see com.alertscape.web.service.AdminService#getTreeDefinition()
   */
  public String getTreeDefinition() {
    return getTreeConfigurationDao().getTreeConfiguration();
  }
  
  /* (non-Javadoc)
   * @see com.alertscape.web.service.AdminService#getAttributeDefinitions()
   */
  public List<AlertAttributeDefinition> getAttributeDefinitions() {
    return getAttributeDefinitionDao().getActiveDefinitions();
  }

  /* (non-Javadoc)
   * @see com.alertscape.web.service.AdminService#saveUser(com.alertscape.common.model.AuthenticatedUser, char[])
   */
  public void saveUser(AuthenticatedUser user, char[] password) {
    LOG.info("Saving user " + user);
    getUserDao().save(user, password);
  }

  /* (non-Javadoc)
   * @see com.alertscape.web.service.AdminService#saveOnramp(com.alertscape.common.model.AlertSource)
   */
  public void saveOnramp(AlertSource onramp) {
    LOG.info("Saving onramp: " + onramp);
    getAlertSourceDao().save(onramp);
  }

  /* (non-Javadoc)
   * @see com.alertscape.web.service.AdminService#saveTreeDefinition(java.lang.String)
   */
  public void saveTreeDefinition(String treeDefinition) {
    getTreeConfigurationDao().save(treeDefinition);
  }
  
  /* (non-Javadoc)
   * @see com.alertscape.web.service.AdminService#saveAttributeDefinition(com.alertscape.common.model.AlertAttributeDefinition)
   */
  public void saveAttributeDefinition(AlertAttributeDefinition definition) {
    getAttributeDefinitionDao().save(definition);
  }

  /**
   * @return the alertSourceDao
   */
  public AlertSourceDao getAlertSourceDao() {
    return alertSourceDao;
  }

  /**
   * @param alertSourceDao
   *          the alertSourceDao to set
   */
  public void setAlertSourceDao(AlertSourceDao alertSourceDao) {
    this.alertSourceDao = alertSourceDao;
  }

  /**
   * @return the userDao
   */
  public AuthenticatedUserDao getUserDao() {
    return userDao;
  }

  /**
   * @param userDao
   *          the userDao to set
   */
  public void setUserDao(AuthenticatedUserDao userDao) {
    this.userDao = userDao;
  }

  /**
   * @return the treeConfigurationDao
   */
  public TreeConfigurationDao getTreeConfigurationDao() {
    return treeConfigurationDao;
  }

  /**
   * @param treeConfigurationDao
   *          the treeConfigurationDao to set
   */
  public void setTreeConfigurationDao(TreeConfigurationDao treeConfigurationDao) {
    this.treeConfigurationDao = treeConfigurationDao;
  }

  /**
   * @return the attributeDefinitionDao
   */
  public AlertAttributeDefinitionDao getAttributeDefinitionDao() {
    return attributeDefinitionDao;
  }

  /**
   * @param attributeDefinitionDao the attributeDefinitionDao to set
   */
  public void setAttributeDefinitionDao(AlertAttributeDefinitionDao attributeDefinitionDao) {
    this.attributeDefinitionDao = attributeDefinitionDao;
  }
}
