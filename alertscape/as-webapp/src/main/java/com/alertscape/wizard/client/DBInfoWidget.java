/**
 * 
 */
package com.alertscape.wizard.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author josh
 * 
 */
public class DBInfoWidget extends AbstractInstallWizardWidget {
  private TextBox driverNameTextBox;
  private TextBox urlTextBox;
  private TextBox usernameTextBox;
  private PasswordTextBox passwordTextBox1;
  private PasswordTextBox passwordTextBox2;
  private Grid g;
  private Button checkButton;
  private Label errorLabel;

  public DBInfoWidget(final InstallWizardServiceAsync wizardService, InstallWizardInfo info) {
    super(wizardService, info);
    FlowPanel layout = new FlowPanel();
    initWidget(layout);

    driverNameTextBox = new TextBox();
    driverNameTextBox.addStyleName("db-text");
    urlTextBox = new TextBox();
    urlTextBox.addStyleName("db-text");
    usernameTextBox = new TextBox();
    usernameTextBox.addStyleName("db-text");
    passwordTextBox1 = new PasswordTextBox();
    passwordTextBox1.addStyleName("db-text");
    passwordTextBox2 = new PasswordTextBox();
    passwordTextBox2.addStyleName("db-text");

    driverNameTextBox.setText("com.mysql.jdbc.Driver");
    urlTextBox.setText("jdbc:mysql://DBHOST/ALERTSCAPE_DATABASE");
    usernameTextBox.setText("alertscape");

    checkButton = new Button("Check");
    checkButton.addClickListener(new SchemaCheckClickListener());
    checkButton.setEnabled(false);

    errorLabel = new Label();

    ValidChangeListener listener = new ValidChangeListener();

    driverNameTextBox.addChangeListener(listener);
    urlTextBox.addChangeListener(listener);
    usernameTextBox.addChangeListener(listener);
    passwordTextBox1.addChangeListener(listener);
    passwordTextBox2.addChangeListener(listener);

    g = new Grid(5, 2);
    int row = 0;
    setRow(row++, "Driver class:", driverNameTextBox);
    setRow(row++, "JDBC URL:", urlTextBox);
    setRow(row++, "Username:", usernameTextBox);
    setRow(row++, "Password:", passwordTextBox1);
    setRow(row++, "Password (again):", passwordTextBox2);

    layout.add(g);

    layout.add(checkButton);
    layout.add(errorLabel);
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
    if (notEmpty(driverNameTextBox) && notEmpty(urlTextBox) && notEmpty(usernameTextBox) && passwordsMatch()) {
      checkButton.setEnabled(true);
    } else {
      checkButton.setEnabled(false);
    }
  }

  protected boolean passwordsMatch() {
    return notEmpty(passwordTextBox1) && notEmpty(passwordTextBox2)
        && passwordTextBox1.getText().equals(passwordTextBox2.getText());
  }

  protected boolean notEmpty(TextBox b) {
    return b.getText().length() > 0;
  }


  private void doDbSuccess() {
    errorLabel.setText("Schema found, press Next to continue");
    getInfo().setDriverName(driverNameTextBox.getText());
    getInfo().setUrl(urlTextBox.getText());
    getInfo().setUsername(usernameTextBox.getText());
    getInfo().setPassword(passwordTextBox1.getText());
    fireProceedable();
  }
  /**
   * @author josh
   * 
   */
  private final class SchemaCheckClickListener implements ClickListener {
    public void onClick(Widget w) {
      String driverName = driverNameTextBox.getText();
      String url = urlTextBox.getText();
      String username = usernameTextBox.getText();
      String password = passwordTextBox1.getText();

      getWizardService().doesAlertscapeSchemaExist(driverName, url, username, password, new AsyncCallback<Boolean>() {
        public void onFailure(Throwable t) {
          errorLabel.setText("Couldn't validate connection settings:" + t.getMessage());
          fireNotProceedable();
        }

        public void onSuccess(Boolean result) {
          errorLabel.setText("");
          if (result) {
            boolean confirm = Window
                .confirm("Found an existing alert schema, which will be used as is and not overwritten. "
                    + "Proceed with install using the existing schema?");
            if (confirm) {
              getInfo().setNoSchemaCreation(true);
              doDbSuccess();
            } else {
              fireNotProceedable();
            }
          } else {
            // Empty schema, continue
            getInfo().setNoSchemaCreation(false);
            doDbSuccess();
          }
        }
      });
    }
  }

  /**
   * @author josh
   * 
   */
  private final class ValidChangeListener implements ChangeListener {
    public void onChange(Widget sender) {
      fireNotProceedable();
      errorLabel.setText("");
      validate();
    }
  }

}
