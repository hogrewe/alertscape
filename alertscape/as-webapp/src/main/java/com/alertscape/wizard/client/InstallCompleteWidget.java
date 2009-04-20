/**
 * 
 */
package com.alertscape.wizard.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author josh
 * 
 */
public class InstallCompleteWidget extends AbstractInstallWizardWidget {

  /**
   * @param wizardService
   * @param info
   */
  public InstallCompleteWidget(InstallWizardServiceAsync wizardService, InstallWizardInfo info) {
    super(wizardService, info);
    VerticalPanel layout = new VerticalPanel();
    initWidget(layout);
    Label l = new Label();

    l.setText("Installation complete.  Please restart the application server.");

    layout.add(l);
  }

  @Override
  public void onShow() {
    fireProceedable();
  }
}
