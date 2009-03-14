/**
 * 
 */
package com.alertscape.wizard.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author josh
 * 
 */
public class HomeDirWidget extends AbstractInstallWizardWidget {
  private VerticalPanel contentPanel = new VerticalPanel();
  private FlowPanel homeDirPanel = new FlowPanel();
  private TextBox homeDirTextBox = new TextBox();
  private Button checkButton = new Button("Check");
  private HTML homeDirPromptLabel = new HTML("Loading...");
  private Label errorLabel = new Label();

  /**
   * 
   */
  public HomeDirWidget(InstallWizardServiceAsync wizardService, InstallWizardInfo info) {
    super(wizardService, info);
    initWidget(contentPanel);
    homeDirPromptLabel.setWordWrap(true);
    homeDirPanel.add(homeDirTextBox);
    homeDirPanel.add(checkButton);

    checkButton.addClickListener(new DirectoryCheckClickListener());

    contentPanel.add(homeDirPromptLabel);
    contentPanel.add(errorLabel);
    contentPanel.add(homeDirPanel);
  }

  public void onShow() {
    homeDirTextBox.setFocus(true);

    AsyncCallback<String> callback = new AsyncCallback<String>() {
      public void onFailure(Throwable caught) {
        homeDirPromptLabel.setText("Could not get user name from the server");
      }

      public void onSuccess(String result) {
        String serverUser = result;
        homeDirPromptLabel.setHTML("Please specify a directory that is writable by user \"" + serverUser
            + "\" on the alertscape server.<br/>This directory will house some of the configuration for "
            + "alertscape, and so should be long-lived. It might be a good idea to create a home directory "
            + "(e.g. /home/alertscape)");
      }
    };
    getWizardService().getServerUser(callback);
  }

  /**
   * @author josh
   * 
   */
  private final class DirectoryCheckClickListener implements ClickListener {
    public void onClick(Widget sender) {
      AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
        public void onFailure(Throwable caught) {
          errorLabel.setText("EXCEPTION!!! " + caught);
        }

        public void onSuccess(Boolean result) {
          if (result) {
            errorLabel.setText("SUCCESS!");
            getInfo().setAsHome(homeDirTextBox.getText());
            fireProceedable();
          } else {
            errorLabel.setText("Couldn't find that directory.");
            fireNotProceedable();
          }
        }
      };
      getWizardService().checkDirectory(homeDirTextBox.getText(), callback);
    }
  }

}
