/**
 * 
 */
package com.alertscape.web.ui.admin.client;

import java.util.List;

import com.alertscape.web.ui.admin.client.model.AttributeDefinition;
import com.alertscape.web.ui.admin.client.model.OnrampDefinition;
import com.alertscape.web.ui.admin.client.model.User;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author josh
 * 
 */
@RemoteServiceRelativePath("adminService.gwt")
public interface AdminGwtService extends RemoteService {
  List<User> getUsers();

  List<OnrampDefinition> getOnramps();

  String getTreeDefinition();

  List<AttributeDefinition> getAttributeDefinitions();

  void saveUser(User user, char[] password);

  void saveOnramp(OnrampDefinition onramp);

  void saveTreeDefinition(String treeDefinition);

  void saveAttributeDefinition(AttributeDefinition definition);
}
