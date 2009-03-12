/**
 * 
 */
package com.alertscape.wizard.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author josh
 * 
 */
@RemoteServiceRelativePath("installWizard")
public interface InstallWizardService extends RemoteService {
  String getServerUser();
  Boolean checkDirectory(String directory);
  String getContext();
  void writeInstallFiles();
}
