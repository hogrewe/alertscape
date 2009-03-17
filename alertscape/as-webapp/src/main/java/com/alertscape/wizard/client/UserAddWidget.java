/**
 * 
 */
package com.alertscape.wizard.client;

import com.alertscape.wizard.client.model.User;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author josh
 * 
 */
public class UserAddWidget extends AbstractInstallWizardWidget {
  private HorizontalPanel layout;
  private FlexTable usersTable;
  private int numUsers;

  private FlexTable inputTable;
  private int numInputs;

  private TextBox username;
  private TextBox fullname;
  private TextBox email;
  private PasswordTextBox password1;
  private PasswordTextBox password2;

  private Button addButton;
  private HTML errorLabel;

  /**
   * @param wizardService
   * @param info
   */
  public UserAddWidget(InstallWizardServiceAsync wizardService, InstallWizardInfo info) {
    super(wizardService, info);

    layout = new HorizontalPanel();
    initWidget(layout);

    VerticalPanel inputLayout = new VerticalPanel();

    usersTable = new FlexTable();
    inputTable = new FlexTable();

    username = new TextBox();
    fullname = new TextBox();
    email = new TextBox();

    password1 = new PasswordTextBox();
    password2 = new PasswordTextBox();

    username.addChangeListener(new ChangeListener() {
      public void onChange(Widget w) {
        if (notEmpty(username) && notEmpty(email) && notEmpty(password1) && notEmpty(password2)
            && password1.getText().equals(password2.getText())) {
          addButton.setEnabled(true);
        } else {
          addButton.setEnabled(false);
        }
      }
    });

    addRow("Username:", username);
    addRow("Password:", password1);
    addRow("Password (again):", password2);
    addRow("Fullname:", fullname);
    addRow("Email:", email);

    addButton = new Button("Add", new ClickListener() {
      public void onClick(Widget w) {
        User user = new User();
        user.setEmail(email.getText());
        user.setUsername(username.getText());
        user.setFullname(fullname.getText());
        user.setPassword(password1.getText());

        getWizardService().addUser(getInfo(), user, new AsyncCallback<User>() {
          public void onFailure(Throwable t) {
            errorLabel.setText("Couldn't add user: " + t.getLocalizedMessage());
          }

          public void onSuccess(User u) {
            addUserToTable(u);
          }
        });
      }

    });

    errorLabel = new HTML("");
    inputLayout.add(inputTable);
    inputLayout.add(addButton);
    inputLayout.add(errorLabel);

    layout.add(usersTable);
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

  protected void addUserToTable(User user) {
    usersTable.setText(numUsers, 0, user.getUsername());
    usersTable.setText(numUsers, 1, user.getFullname());
    usersTable.setText(numUsers, 2, user.getEmail());
    numUsers++;

    fireProceedable();
  }

}
