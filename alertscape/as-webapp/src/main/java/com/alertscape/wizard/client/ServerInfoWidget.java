/**
 * 
 */
package com.alertscape.wizard.client;

import com.google.gwt.core.client.GWT;
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
public class ServerInfoWidget extends WizardContent {
  private VerticalPanel contentPanel = new VerticalPanel();
  private Label errorLabel = new Label();
  private Grid g;
  private TextBox serverContext;
  private InstallWizardServiceAsync wizardService;

  public ServerInfoWidget() {
    super();
    initWidget(contentPanel);
    wizardService = GWT.create(InstallWizardService.class);
    
    contentPanel.add(errorLabel);

    g = new Grid(1, 2);
    serverContext = new TextBox();
    setRow(0, "Server Context:", serverContext);

    ChangeListener listener = new ValidChangeListener();
    serverContext.addChangeListener(listener);
    
    contentPanel.add(g);

    wizardService.getContext(new AsyncCallback<String>() {
      public void onFailure(Throwable caught) {
        errorLabel.setText("Couldn't talk to server to get context");
      }

      public void onSuccess(String result) {
        serverContext.setText(result);
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
    if (notEmpty(serverContext)) {
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
