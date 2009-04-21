/**
 * 
 */
package com.alertscape.web.service;

import java.util.List;

import com.alertscape.common.model.AlertAttributeDefinition;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.AuthenticatedUser;

/**
 * @author josh
 *
 */
public interface AdminService {

  abstract List<AuthenticatedUser> getUsers();

  abstract List<AlertSource> getOnramps();

  abstract String getTreeDefinition();

  abstract List<AlertAttributeDefinition> getAttributeDefinitions();

  abstract void saveUser(AuthenticatedUser user, char[] password);

  abstract void saveOnramp(AlertSource onramp);

  abstract void saveTreeDefinition(String treeDefinition);

  abstract void saveAttributeDefinition(AlertAttributeDefinition definition);

}