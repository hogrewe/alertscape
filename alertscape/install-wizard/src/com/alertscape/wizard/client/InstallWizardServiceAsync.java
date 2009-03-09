package com.alertscape.wizard.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InstallWizardServiceAsync {
  void getServerUser(AsyncCallback<String> callback);
  void checkDirectory(String directory, AsyncCallback<Boolean> callback);
}
