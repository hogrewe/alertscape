/**
 * 
 */
package com.alertscape.wizard.client;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author josh
 * 
 */
public class SetDBInfoWidget extends WizardContent {
  private TextBox driverNameTextBox;
  private TextBox urlTextBox;
  private TextBox usernameTextBox;
  private PasswordTextBox passwordTextBox1;
  private PasswordTextBox passwordTextBox2;
  private Grid g;

  public SetDBInfoWidget() {
    FlowPanel layout = new FlowPanel();
    initWidget(layout);
    
    driverNameTextBox = new TextBox();
    urlTextBox = new TextBox();
    usernameTextBox = new TextBox();
    passwordTextBox1 = new PasswordTextBox();
    passwordTextBox2 = new PasswordTextBox();

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
      fireProceedable();
    } else {
      fireNotProceedable();
    }
  }

  protected boolean passwordsMatch() {
    return notEmpty(passwordTextBox1) && notEmpty(passwordTextBox2)
        && passwordTextBox1.getText().equals(passwordTextBox2.getText());
  }

  protected boolean notEmpty(TextBox b) {
    return b.getText().length() > 0;
  }

  /**
   * @author josh
   * 
   */
  private final class ValidChangeListener implements ChangeListener {
    public void onChange(Widget sender) {
      validate();
    }
  }

}
