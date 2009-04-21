/**
 * 
 */
package com.alertscape.web.ui.admin.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.alertscape.web.ui.admin.client.AdminGwtServiceAsync;
import com.alertscape.web.ui.admin.client.model.OnrampDefinition;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author josh
 * 
 */
public class OnrampAdminWidget extends AbstractAdminComponent {
  private VerticalPanel layout;
  private FlexTable onrampsTable;
  private List<OnrampDefinition> existingDefinitions;
  private int numOnramps;

  private FlexTable inputTable;
  private int numInputs;

  private TextBox name;
  private ListBox type;
  private TextArea configuration;

  private Button saveButton;
  private HTML errorLabel;
  private OnrampDefinition selectedOnramp;

  /**
   * @param adminService
   * @param info
   */
  public OnrampAdminWidget(final AdminGwtServiceAsync adminService) {
    super(adminService);
    layout = new VerticalPanel();
    initWidget(layout);

    VerticalPanel inputLayout = new VerticalPanel();

    VerticalPanel onrampsTableLayout = new VerticalPanel();
    onrampsTable = new FlexTable();
    existingDefinitions = new ArrayList<OnrampDefinition>();
    
    onrampsTable.addTableListener(new TableListener() {
      public void onCellClicked(SourcesTableEvents source, int row, int cell) {
        setSelectedOnramp(existingDefinitions.get(row));
      }
    });
    HorizontalPanel buttonPanel = new HorizontalPanel();
    Button newButton = new Button("New Onramp");
    newButton.addClickListener(new ClickListener() {
      public void onClick(Widget w) {
        setSelectedOnramp(null);
      }
    });
    buttonPanel.add(newButton);
    onrampsTableLayout.add(onrampsTable);
    onrampsTableLayout.add(buttonPanel);

    inputTable = new FlexTable();

    name = new TextBox();
    type = new ListBox();
    type.setVisibleItemCount(1);
    type.addItem("DB");
    type.addItem("File");
    configuration = new TextArea();
    configuration.setVisibleLines(25);
    configuration.setCharacterWidth(100);

    ValidInputChangeListener validInputChangeListener = new ValidInputChangeListener();
    name.addChangeListener(validInputChangeListener);
    configuration.addChangeListener(validInputChangeListener);

    addRow("Name:", name);
    addRow("Type:", type);
    addRow("Configuration:", configuration);

    saveButton = new Button("Save", new ClickListener() {
      public void onClick(Widget w) {
        if(selectedOnramp == null) {
          selectedOnramp = new OnrampDefinition();
        }
        selectedOnramp.setConfiguration(configuration.getText());
        selectedOnramp.setName(name.getText());
        selectedOnramp.setType(type.getItemText(type.getSelectedIndex()));

        getAdminService().saveOnramp(selectedOnramp, new AsyncCallback<Void>() {
          public void onFailure(Throwable t) {
            errorLabel.setText("Couldn't add onramp: " + t.getLocalizedMessage());
          }

          public void onSuccess(Void result) {
            setSelectedOnramp(null);
            loadOnramps();
          }
        });
      }

    });

    saveButton.setEnabled(false);

    errorLabel = new HTML("");
    inputLayout.add(inputTable);
    inputLayout.add(saveButton);
    inputLayout.add(errorLabel);

    layout.add(onrampsTableLayout);
    layout.add(inputLayout);
  }

  @Override
  public void onShow() {


    loadOnramps();
  }
  
  private void setSelectedOnramp(OnrampDefinition def) {
    selectedOnramp = def;
    if(def == null) {
      name.setText(null);
      type.setSelectedIndex(0);
      configuration.setText(null);
      saveButton.setEnabled(false);
    } else {
      name.setText(def.getName());
      for(int i=0; i< type.getItemCount(); i++) {
        if(type.getItemText(i).equals(def.getType())) {
          type.setSelectedIndex(i);
          break;
        }
      }
      configuration.setText(def.getConfiguration());
      saveButton.setEnabled(true);
    }
  }

  private void loadOnramps() {
    getAdminService().getOnramps(new AsyncCallback<List<OnrampDefinition>>() {
      public void onFailure(Throwable t) {
        errorLabel.setText("Couldn't load onramps: " + t.getLocalizedMessage());
      }

      public void onSuccess(List<OnrampDefinition> definitions) {
        numOnramps = 0;
        onrampsTable.clear();
        existingDefinitions.clear();
        setSelectedOnramp(null);
        
        for (OnrampDefinition onramp : definitions) {
          addOnrampToTable(onramp);
        }
        errorLabel.setText(null);
      }
    });
  }

  protected void addRow(String label, Widget w) {
    inputTable.setText(numInputs, 0, label);
    inputTable.setWidget(numInputs, 1, w);
    numInputs++;
  }

  protected void addOnrampToTable(OnrampDefinition onramp) {
    onrampsTable.setText(numOnramps, 0, onramp.getName());
    onrampsTable.setText(numOnramps, 1, onramp.getType());
    String config = onramp.getConfiguration();
    onrampsTable.setText(numOnramps, 2, config != null && config.length() > 20 ? config.substring(0, 20) : config);
    numOnramps++;
    existingDefinitions.add(onramp);
  }

  /**
   * @author josh
   * 
   */
  private final class ValidInputChangeListener implements ChangeListener {
    public void onChange(Widget w) {
      if (notEmpty(name) && notEmpty(configuration)) {
        saveButton.setEnabled(true);
      } else {
        saveButton.setEnabled(false);
      }
    }
  }

}