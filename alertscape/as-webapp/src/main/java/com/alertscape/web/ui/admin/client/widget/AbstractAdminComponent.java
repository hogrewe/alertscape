/**
 * 
 */
package com.alertscape.web.ui.admin.client.widget;

import com.alertscape.web.ui.admin.client.AdminGwtServiceAsync;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author josh
 * 
 */
public abstract class AbstractAdminComponent extends AdminComponent {
  private AdminGwtServiceAsync adminService;

  public AbstractAdminComponent(AdminGwtServiceAsync adminService) {
    this.adminService = adminService;
  }

  protected boolean notEmpty(TextBox b) {
    return b.getText().length() > 0;
  }

  protected boolean notEmpty(TextArea b) {
    return b.getText().length() > 0;
  }

  /**
   * @return the adminService
   */
  public AdminGwtServiceAsync getAdminService() {
    return adminService;
  }

  /**
   * @param adminService
   *          the adminService to set
   */
  public void setAdminService(AdminGwtServiceAsync adminService) {
    this.adminService = adminService;
  }
}
