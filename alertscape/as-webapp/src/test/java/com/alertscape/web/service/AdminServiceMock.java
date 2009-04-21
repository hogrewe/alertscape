/**
 * 
 */
package com.alertscape.web.service;

import java.util.ArrayList;
import java.util.List;

import com.alertscape.common.model.AlertAttributeDefinition;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.AlertSourceType;
import com.alertscape.common.model.AuthenticatedUser;

/**
 * @author josh
 * 
 */
public class AdminServiceMock implements AdminService {
  private List<AlertAttributeDefinition> definitions;
  private List<AlertSource> onramps;
  private List<AuthenticatedUser> users;

  public AdminServiceMock() {
    definitions = new ArrayList<AlertAttributeDefinition>();
    AlertAttributeDefinition def = new AlertAttributeDefinition();
    def.setDisplayName("customer");
    def.setName("customer");
    def.setMaxSize(20);
    definitions.add(def);

    onramps = new ArrayList<AlertSource>();
    AlertSource onramp = new AlertSource(1, "Onramp1");
    onramp.setConfigXml("<xml-config/>");
    AlertSourceType type = new AlertSourceType();
    type.setType("DB");
    onramp.setType(type);
    onramps.add(onramp);

    new AlertSource(1, "Onramp2");
    onramp.setConfigXml("<xml-config-multiple/>");
    type = new AlertSourceType();
    type.setType("File");
    onramp.setType(type);
    onramps.add(onramp);

    users = new ArrayList<AuthenticatedUser>();
    AuthenticatedUser user = new AuthenticatedUser();
    user.setEmail("joeblow@example.com");
    user.setFullName("Joseph Blow");
    user.setUsername("joeblow");
    users.add(user);

    user = new AuthenticatedUser();
    user.setEmail("foobar@example.com");
    user.setFullName("Foostopher Bar");
    user.setUsername("foobar");
    users.add(user);
  }

  @Override
  public List<AlertAttributeDefinition> getAttributeDefinitions() {
    return definitions;
  }

  @Override
  public List<AlertSource> getOnramps() {
    return onramps;
  }

  @Override
  public String getTreeDefinition() {
    return "Tree Def!";
  }

  @Override
  public List<AuthenticatedUser> getUsers() {
    return users;
  }

  @Override
  public void saveAttributeDefinition(AlertAttributeDefinition definition) {
    // TODO Auto-generated method stub

  }

  @Override
  public void saveOnramp(AlertSource onramp) {
    // TODO Auto-generated method stub

  }

  @Override
  public void saveTreeDefinition(String treeDefinition) {
    // TODO Auto-generated method stub

  }

  @Override
  public void saveUser(AuthenticatedUser user, char[] password) {
    // TODO Auto-generated method stub

  }

}
