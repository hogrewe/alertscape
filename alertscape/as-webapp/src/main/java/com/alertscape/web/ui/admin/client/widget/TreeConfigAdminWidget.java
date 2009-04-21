package com.alertscape.web.ui.admin.client.widget;

import com.alertscape.web.ui.admin.client.AdminGwtServiceAsync;
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
public class TreeConfigAdminWidget extends AbstractAdminComponent {

  private TextArea config;
  private HTML errorLabel;

  /**
   * @param adminService
   * @param info
   */
  public TreeConfigAdminWidget(AdminGwtServiceAsync adminService) {
    super(adminService);
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

        getAdminService().saveTreeDefinition(config.getText(), new AsyncCallback<Void>() {
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
    getAdminService().getTreeDefinition(new AsyncCallback<String>() {
      public void onFailure(Throwable t) {
        errorLabel.setText("Couldn't get tree configuration: " + t.getLocalizedMessage());
      }

      public void onSuccess(String configText) {
        config.setText(configText);
      }
    });
  }
}
