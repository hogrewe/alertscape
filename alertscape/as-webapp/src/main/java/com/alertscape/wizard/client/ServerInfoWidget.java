/**
 * 
 */
package com.alertscape.wizard.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author josh
 * 
 */
public class ServerInfoWidget extends AbstractInstallWizardWidget {
  private VerticalPanel contentPanel = new VerticalPanel();
  private Label errorLabel = new Label();
  private Grid g;
  private TextBox serverContextTextBox;
  private TextBox jmsPortTextBox;

  public ServerInfoWidget(InstallWizardServiceAsync wizardService, InstallWizardInfo info) {
    super(wizardService, info);
    initWidget(contentPanel);

    contentPanel.add(errorLabel);

    g = new Grid(2, 2);
    serverContextTextBox = new TextBox();
    jmsPortTextBox = new TextBox();
    jmsPortTextBox.setText("7777");

    int row = 0;
    setRow(row++, "Server Context:", serverContextTextBox);
    setRow(row++, "JMS Port:", jmsPortTextBox);

    ChangeListener listener = new ValidChangeListener();
    serverContextTextBox.addChangeListener(listener);
    jmsPortTextBox.addChangeListener(listener);

    contentPanel.add(g);

    getWizardService().getContext(new AsyncCallback<String>() {
      public void onFailure(Throwable caught) {
        errorLabel.setText("Couldn't talk to server to get context");
      }

      public void onSuccess(String result) {
        serverContextTextBox.setText(result);
      }

    });

  }

  @Override
  public void onShow() {
    validate();
  }

  protected void setRow(int row, String label, Widget w) {
    g.setText(row, 0, label);
    g.setWidget(row, 1, w);
  }

  protected void validate() {
    if (notEmpty(serverContextTextBox) && notEmpty(jmsPortTextBox)) {
      try {
        Integer.parseInt(jmsPortTextBox.getText());
      } catch (NumberFormatException e) {
      }
      getInfo().setContext(serverContextTextBox.getText());
      getInfo().setJmsPort(jmsPortTextBox.getText());
      fireProceedable();
    } else {
      fireNotProceedable();
    }
  }

  protected boolean notEmpty(TextBox b) {
    return b.getText().length() > 0;
  }

  private final class ValidChangeListener implements ChangeListener {
    public void onChange(Widget sender) {
      validate();
    }
  }

}
