/**
 * 
 */
package com.alertscape.wizard.client;

import com.alertscape.wizard.client.model.OnrampDefinition;
import com.alertscape.wizard.client.model.User;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author josh
 * 
 */
@RemoteServiceRelativePath("installWizard")
public interface InstallWizardService extends RemoteService {
  String getServerUser();

  boolean checkDirectory(String directory);

  String getContext();
  
  String getServerName();

  boolean doesAlertscapeSchemaExist(String driverName, String url, String username, String password)
      throws WizardException;

  void install(InstallWizardInfo info) throws WizardException;
  
  User addUser(InstallWizardInfo info, User user) throws WizardException;
  
  void setTreeConfig(InstallWizardInfo info, String treeConfig) throws WizardException;
  
  OnrampDefinition addOnramp(InstallWizardInfo info, OnrampDefinition onramp) throws WizardException;
  
}
