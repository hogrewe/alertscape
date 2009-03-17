package com.alertscape.wizard.client;

import com.alertscape.wizard.client.model.OnrampDefinition;
import com.alertscape.wizard.client.model.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InstallWizardServiceAsync {
  void getServerUser(AsyncCallback<String> callback);

  void checkDirectory(String directory, AsyncCallback<Boolean> callback);

  void getContext(AsyncCallback<String> callback);

  void getServerName(AsyncCallback<String> callback);

  void doesAlertscapeSchemaExist(String driverName, String url, String username, String password,
      AsyncCallback<Boolean> callback);

  void install(InstallWizardInfo info, AsyncCallback<Void> callback);

  void addUser(InstallWizardInfo info, User user, AsyncCallback<User> callback);

  void setTreeConfig(InstallWizardInfo info, String treeConfig, AsyncCallback<Void> callback);

  void addOnramp(InstallWizardInfo info, OnrampDefinition onramp, AsyncCallback<OnrampDefinition> callback);

}
