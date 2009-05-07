package com.alertscape.web.ui.admin.client;

import java.util.List;
import java.util.Map;

import com.alertscape.web.ui.admin.client.model.AttributeDefinition;
import com.alertscape.web.ui.admin.client.model.OnrampDefinition;
import com.alertscape.web.ui.admin.client.model.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AdminGwtServiceAsync {
  void getOnramps(AsyncCallback<List<OnrampDefinition>> callback);

  void getTreeDefinition(AsyncCallback<String> callback);

  void getUsers(AsyncCallback<List<User>> callback);

  void getAttributeDefinitions(AsyncCallback<List<AttributeDefinition>> callback);

  void saveOnramp(OnrampDefinition onramp, AsyncCallback<Void> callback);

  void saveTreeDefinition(String treeDefinition, AsyncCallback<Void> callback);

  void saveUser(User user, char[] password, AsyncCallback<Void> callback);

  void saveAttributeDefinition(AttributeDefinition definition, AsyncCallback<Void> callback);

  void regexTest(String regex, Map<String, String> replacements, String[] testStrings,
      AsyncCallback<List<Map<String, String>>> callback);
}
