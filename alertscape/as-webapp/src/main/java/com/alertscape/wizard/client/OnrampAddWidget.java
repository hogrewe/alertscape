/**
 * 
 */
package com.alertscape.wizard.client;

import com.alertscape.wizard.client.model.OnrampDefinition;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author josh
 * 
 */
public class OnrampAddWidget extends AbstractInstallWizardWidget {
  /**
   * @author josh
   * 
   */
  private final class ValidInputChangeListener implements ChangeListener {
    public void onChange(Widget w) {
      if (notEmpty(name) && notEmpty(configuration)) {
        addButton.setEnabled(true);
      } else {
        addButton.setEnabled(false);
      }
    }
  }

  private HorizontalPanel layout;
  private FlexTable onrampsTable;
  private int numOnramps;

  private FlexTable inputTable;
  private int numInputs;

  private TextBox name;
  private ListBox type;
  private TextArea configuration;

  private Button addButton;
  private HTML errorLabel;

  /**
   * @param wizardService
   * @param info
   */
  public OnrampAddWidget(InstallWizardServiceAsync wizardService, InstallWizardInfo info) {
    super(wizardService, info);

    layout = new HorizontalPanel();
    initWidget(layout);

    VerticalPanel inputLayout = new VerticalPanel();

    onrampsTable = new FlexTable();
    inputTable = new FlexTable();

    name = new TextBox();
    type = new ListBox();
    type.setVisibleItemCount(1);
    type.addItem("DB");
    type.addItem("File");
    configuration = new TextArea();
    configuration.setVisibleLines(25);

    ValidInputChangeListener validInputChangeListener = new ValidInputChangeListener();
    name.addChangeListener(validInputChangeListener);
    configuration.addChangeListener(validInputChangeListener);

    addRow("Username:", name);
    addRow("Type:", type);
    addRow("Configuration:", configuration);

    addButton = new Button("Add", new ClickListener() {
      public void onClick(Widget w) {
        OnrampDefinition onramp = new OnrampDefinition();
        onramp.setConfiguration(configuration.getText());
        onramp.setName(name.getText());
        onramp.setType(type.getItemText(type.getSelectedIndex()));

        getWizardService().addOnramp(getInfo(), onramp, new AsyncCallback<OnrampDefinition>() {
          public void onFailure(Throwable t) {
            errorLabel.setText("Couldn't add onramp: " + t.getLocalizedMessage());
          }

          public void onSuccess(OnrampDefinition ramp) {
            addOnrampToTable(ramp);
          }
        });
      }

    });

    errorLabel = new HTML("");
    inputLayout.add(inputTable);
    inputLayout.add(addButton);
    inputLayout.add(errorLabel);

    layout.add(onrampsTable);
    layout.add(inputLayout);
  }

  @Override
  public void onShow() {
  }

  protected void addRow(String label, Widget w) {
    inputTable.setText(numInputs, 0, label);
    inputTable.setWidget(numInputs, 1, w);
    numInputs++;
  }

  protected void addOnrampToTable(OnrampDefinition onramp) {
    onrampsTable.setText(numOnramps, 0, onramp.getName());
    onrampsTable.setText(numOnramps, 1, onramp.getType());
    onrampsTable.setText(numOnramps, 2, onramp.getConfiguration().substring(0, 30));
    numOnramps++;

    fireProceedable();
  }
}
