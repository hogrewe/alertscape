/**
 * 
 */
package com.alertscape.wizard.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author josh
 * 
 */
public class TreeConfigWidget extends AbstractInstallWizardWidget {

  private TextArea config;
  private HTML errorLabel;

  /**
   * @param wizardService
   * @param info
   */
  public TreeConfigWidget(InstallWizardServiceAsync wizardService, InstallWizardInfo info) {
    super(wizardService, info);
    VerticalPanel layout = new VerticalPanel();
    errorLabel = new HTML();

    initWidget(layout);
    config = new TextArea();
    config.setVisibleLines(25);
    config.setCharacterWidth(130);

    layout.add(config);

    Button setButton = new Button("Set", new ClickListener() {
      public void onClick(Widget w) {
        errorLabel.setText("");

        getWizardService().setTreeConfig(getInfo(), config.getText(), new AsyncCallback<Void>() {
          public void onFailure(Throwable t) {
            errorLabel.setText("Couldn't set tree configuration: " + t.getLocalizedMessage());
          }

          public void onSuccess(Void result) {
            errorLabel.setText("Successfully set tree configuration");
          }
        });
      }
    });

    layout.add(setButton);
  }

  @Override
  public void onShow() {
    getWizardService().getTreeConfiguration(getInfo(), new AsyncCallback<String>() {
      public void onFailure(Throwable t) {
        errorLabel.setText("Couldn't get tree configuration: " + t.getLocalizedMessage());
      }

      public void onSuccess(String configText) {
        config.setText(configText);
        fireProceedable();
      }
    });
  }
}
