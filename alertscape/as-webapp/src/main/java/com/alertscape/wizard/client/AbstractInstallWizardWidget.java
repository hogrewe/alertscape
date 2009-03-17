/**
 * 
 */
package com.alertscape.wizard.client;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author josh
 * 
 */
public abstract class AbstractInstallWizardWidget extends WizardContent {
  private InstallWizardServiceAsync wizardService;
  private InstallWizardInfo info;

  public AbstractInstallWizardWidget(InstallWizardServiceAsync wizardService, InstallWizardInfo info) {
    this.wizardService = wizardService;
    this.info = info;
  }

  /**
   * @return the wizardService
   */
  public InstallWizardServiceAsync getWizardService() {
    return wizardService;
  }

  /**
   * @param wizardService
   *          the wizardService to set
   */
  public void setWizardService(InstallWizardServiceAsync wizardService) {
    this.wizardService = wizardService;
  }

  /**
   * @return the info
   */
  public InstallWizardInfo getInfo() {
    return info;
  }

  /**
   * @param info
   *          the info to set
   */
  public void setInfo(InstallWizardInfo info) {
    this.info = info;
  }

  protected boolean notEmpty(TextBox b) {
    return b.getText().length() > 0;
  }
  
  protected boolean notEmpty(TextArea b) {
    return b.getText().length() > 0;
  }

  
}
