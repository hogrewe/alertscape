package com.alertscape.web.ui.admin.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.alertscape.web.ui.admin.client.AdminGwtServiceAsync;
import com.alertscape.web.ui.admin.client.model.User;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
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
public class UserAdminWidget extends AbstractAdminComponent {
  private VerticalPanel layout;
  private FlexTable usersTable;
  private int numUsers;
  private List<User> users;

  private Button addButton;

  private HTML errorLabel;

  /**
   * @param adminService
   * @param info
   */
  public UserAdminWidget(AdminGwtServiceAsync adminService) {
    super(adminService);

    users = new ArrayList<User>();

    layout = new VerticalPanel();
    initWidget(layout);

    usersTable = new FlexTable();
    layout.add(usersTable);
    addButton = new Button("Add User");
    addButton.addClickListener(new ClickListener() {
      public void onClick(Widget w) {
        editUser(new User());
      }
    });

    usersTable.addTableListener(new TableListener() {
      public void onCellClicked(SourcesTableEvents source, int row, int cell) {
        editUser(users.get(row));
      }
    });

    layout.add(addButton);

    errorLabel = new HTML();
    layout.add(errorLabel);
  }

  @Override
  public void onShow() {
    numUsers = 0;
    usersTable.clear();
    loadUsers();
  }

  private void loadUsers() {
    getAdminService().getUsers(new AsyncCallback<List<User>>() {
      public void onFailure(Throwable t) {
        errorLabel.setText("Couldn't get users: " + t.getLocalizedMessage());
      }

      public void onSuccess(List<User> users) {
        numUsers = 0;
        usersTable.clear();
        for (User user : users) {
          addUserToTable(user);
        }
        UserAdminWidget.this.users = users;
      }
    });
  }

  protected void addUserToTable(User user) {
    usersTable.setText(numUsers, 0, user.getUsername());
    usersTable.setText(numUsers, 1, user.getFullname());
    usersTable.setText(numUsers, 2, user.getEmail());
    numUsers++;
  }

  private void editUser(User user) {
    final UserEditDialog editDialog = new UserEditDialog(user, getAdminService());
    editDialog.addPopupListener(new PopupListener() {
      public void onPopupClosed(PopupPanel popup, boolean autoClosed) {
        loadUsers();
      }
    });
    editDialog.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
      public void setPosition(int offsetWidth, int offsetHeight) {
        int left = (Window.getClientWidth() - offsetWidth) / 2;
        int top = (Window.getClientHeight() - offsetHeight) / 2;
        editDialog.setPopupPosition(left, top);
      }
    });

  }

  private static class UserEditDialog extends DialogBox {
    private User user;
    private AdminGwtServiceAsync adminService;

    private TextBox username;
    private TextBox fullname;
    private TextBox email;
    private PasswordTextBox password1;
    private PasswordTextBox password2;

    private Button saveButton;
    private HTML errorLabel;

    private FlexTable inputTable;
    private int numInputs;

    public UserEditDialog(final User user, final AdminGwtServiceAsync adminService) {
      this.user = user;
      this.adminService = adminService;
      
      if(user.getUsername() == null) {
        setTitle("New User");
      } else {
        setTitle("Editing " + user.getUsername());
      }

      VerticalPanel inputLayout = new VerticalPanel();
      this.setWidget(inputLayout);

      inputTable = new FlexTable();

      username = new TextBox();
      fullname = new TextBox();
      email = new TextBox();

      password1 = new PasswordTextBox();
      password2 = new PasswordTextBox();

      InputValidationChangeListener listener = new InputValidationChangeListener();
      username.addChangeListener(listener);
      fullname.addChangeListener(listener);
      email.addChangeListener(listener);
      password1.addChangeListener(listener);
      password2.addChangeListener(listener);

      addRow("Username:", username);
      addRow("Password:", password1);
      addRow("Password (again):", password2);
      addRow("Fullname:", fullname);
      addRow("Email:", email);

      saveButton = new Button("Save", new ClickListener() {
        public void onClick(Widget w) {
          user.setEmail(email.getText());
          user.setUsername(username.getText());
          user.setFullname(fullname.getText());

          adminService.saveUser(user, password1.getText().toCharArray(), new AsyncCallback<Void>() {
            public void onFailure(Throwable t) {
              errorLabel.setText("Couldn't save user: " + t.getLocalizedMessage());
            }

            public void onSuccess(Void result) {
              hide();
            }
          });
        }
      });

      fullname.setText(user.getFullname());
      username.setText(user.getUsername());
      email.setText(user.getEmail());

      listener.onChange(username);

      errorLabel = new HTML("");
      inputLayout.add(inputTable);
      inputLayout.add(saveButton);
      inputLayout.add(errorLabel);

    }

    protected void addRow(String label, Widget w) {
      inputTable.setText(numInputs, 0, label);
      inputTable.setWidget(numInputs, 1, w);
      numInputs++;
    }

    private final class InputValidationChangeListener implements ChangeListener {
      public void onChange(Widget w) {
        boolean enabled = notEmpty(username) && notEmpty(email);
        if(notEmpty(password1) || notEmpty(password2)) {
          enabled &= password1.getText().equals(password2.getText());
        }
        saveButton.setEnabled(enabled);
      }
    }

    protected boolean notEmpty(TextBox b) {
      return b.getText().length() > 0;
    }

    protected boolean notEmpty(TextArea b) {
      return b.getText().length() > 0;
    }

  }

}
