/**
 * 
 */
package com.alertscape.web.ui.admin.client.widget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alertscape.web.ui.admin.client.AdminGwtServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author josh
 * 
 */
public class OnrampTesterWidget extends AbstractAdminComponent {

  private FlexTable resultsTable;
  private FlexTable inputTable;
  private TextBox regexText;
  private TextBox replaceField;
  private TextBox replaceString;

  private TextArea candidateString;
  private Button testButton;

  int numInputs;

  /**
   * @param adminService
   */
  public OnrampTesterWidget(AdminGwtServiceAsync adminService) {
    super(adminService);

    VerticalPanel layout = new VerticalPanel();
    initWidget(layout);

    resultsTable = new FlexTable();
    inputTable = new FlexTable();
    regexText = new TextBox();
    replaceField = new TextBox();
    replaceString = new TextBox();

    candidateString = new TextArea();

    testButton = new Button("Test");

    addRow("Regex:", regexText);
    addRow("Replacement Field:", replaceField);
    addRow("Replacement String:", replaceString);

    layout.add(inputTable);
    layout.add(candidateString);
    layout.add(testButton);
    layout.add(resultsTable);

    testButton.addClickListener(new ClickListener() {
      public void onClick(Widget w) {
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put(replaceField.getText(), replaceString.getText());
        final String[] testStrings = { candidateString.getText() };
        getAdminService().regexTest(regexText.getText(), replacements, testStrings,
            new AsyncCallback<List<Map<String, String>>>() {
              public void onFailure(Throwable t) {
                // TODO Auto-generated method stub

              }

              public void onSuccess(List<Map<String, String>> values) {
                resultsTable.clear();
                for (int i = 0; i < testStrings.length; i++) {
                  resultsTable.setText(i, 0, testStrings[i]);
                  Map<String, String> alert = values.get(i);
                  String alertString = "No match";
                  if (alert != null) {
                    StringBuilder builder = new StringBuilder();
                    for (String fieldName : alert.keySet()) {
                      builder.append(fieldName + ": " + alert.get(fieldName) + "\n");
                    }
                  }
                  resultsTable.setText(i, 1, alertString);
                }
              }

            });
      }

    });

  }

  @Override
  public void onShow() {
  }

  protected void addRow(String label, Widget w) {
    inputTable.setText(numInputs, 0, label);
    inputTable.setWidget(numInputs, 1, w);
    numInputs++;
  }
}
