/**
 * 
 */
package com.alertscape.wizard.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author josh
 * 
 */
public class FinalInstallWidget extends AbstractInstallWizardWidget {
  private HTML errorLabel;
  private VerticalPanel layout;

  /**
   * @param wizardService
   * @param info
   */
  public FinalInstallWidget(InstallWizardServiceAsync wizardService, InstallWizardInfo info) {
    super(wizardService, info);
    layout = new VerticalPanel();
    initWidget(layout);

  }

  @Override
  public void onShow() {
    layout.clear();
    
    errorLabel = new HTML();
    layout.add(errorLabel);

    SummarySection db = new SummarySection(4, "Database");
    db.addRow("Driver class:", getInfo().getDriverName());
    db.addRow("URL:", getInfo().getUrl());
    db.addRow("Username:", getInfo().getUsername());
    db.addRow("Password", "*********");
    layout.add(db);

    SummarySection server = new SummarySection(3, "Server Info");
    server.addRow("Server context:", getInfo().getContext());
    server.addRow("JMS Port:", getInfo().getJmsPort());
    server.addRow("Home directory:", getInfo().getAsHome());
    layout.add(server);

    Button install = new Button("Install", new ClickListener() {
      public void onClick(Widget w) {
        getWizardService().install(getInfo(), new AsyncCallback<Void>() {
          public void onFailure(Throwable t) {
            errorLabel.setText("Couldn't install Alertscape: " + t.getLocalizedMessage());
          }

          public void onSuccess(Void value) {
            errorLabel.setText("Finished installing, please restart application server");
          }

        });
      }
    });

    layout.add(install);
  }

  private class SummarySection extends Composite {
    private Grid g;
    private int row;

    public SummarySection(int rowCount, String title) {
      g = new Grid(rowCount, 2);
      DecoratorPanel p = new DecoratorPanel();
      p.setTitle(title);
      initWidget(p);
      p.setWidget(g);
    }

    public void addRow(String label, String value) {
      g.setText(row, 0, label);
      g.setText(row, 1, value);
      row++;
    }

  }

}
