package com.alertscape.wizard.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InstallWizardServiceAsync {
  void getServerUser(AsyncCallback<String> callback);

  void checkDirectory(String directory, AsyncCallback<Boolean> callback);

  void getContext(AsyncCallback<String> callback);

  void doesAlertscapeSchemaExist(String driverName, String url, String username, String password,
      AsyncCallback<Boolean> callback);

  void install(InstallWizardInfo info, AsyncCallback<Void> callback);
}
