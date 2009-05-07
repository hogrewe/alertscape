/**
 * 
 */
package com.alertscape.web.ui.admin.client;

import java.util.HashMap;
import java.util.Map;

import com.alertscape.web.ui.admin.client.widget.AdminComponent;
import com.alertscape.web.ui.admin.client.widget.OnrampAdminWidget;
import com.alertscape.web.ui.admin.client.widget.OnrampTesterWidget;
import com.alertscape.web.ui.admin.client.widget.TreeConfigAdminWidget;
import com.alertscape.web.ui.admin.client.widget.UserAdminWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;

/**
 * @author josh
 * 
 */
public class AdminContainer extends Composite {

  private SimplePanel adminContentPanel;
  private AdminGwtServiceAsync adminService;
  private Map<String, AdminNavLink> navLinkMap;
  private Map<TreeItem, AdminNavLink> treeItemToNav;

  public AdminContainer() {
    HorizontalPanel mainPanel = new HorizontalPanel();
    initWidget(mainPanel);

    adminService = GWT.create(AdminGwtService.class);

    navLinkMap = new HashMap<String, AdminNavLink>();
    treeItemToNav = new HashMap<TreeItem, AdminNavLink>();

    History.addHistoryListener(new HistoryListener() {
      public void onHistoryChanged(String historyToken) {
        AdminNavLink adminNavLink = navLinkMap.get(historyToken);
        activateNav(adminNavLink);
      }
    });

    // Create a new stack panel
    DecoratedStackPanel stackPanel = new DecoratedStackPanel();
    stackPanel.setWidth("200px");

    String adminHeader = getHeaderString("Admin", null);

    stackPanel.add(makeAdminPanel(), adminHeader, true);

    mainPanel.add(stackPanel);

    adminContentPanel = new SimplePanel();
    HorizontalPanel contentPanel = new HorizontalPanel();

    DecoratorPanel contentDecorator = new DecoratorPanel();
    contentDecorator.setWidget(adminContentPanel);

    contentPanel.add(contentDecorator);

    mainPanel.add(contentPanel);

    mainPanel.setWidth("100%");
    contentDecorator.setWidth("100%");
    contentPanel.setWidth("100%");
  }

  private Tree makeAdminPanel() {
    Tree adminTree = new Tree();
    adminTree.addTreeListener(new AdminTreeListener());
    addNavLink(adminTree, new AdminNavLink("Users", new UserAdminWidget(adminService)));
    addNavLink(adminTree, new AdminNavLink("Onramps", new OnrampAdminWidget(adminService)));
    addNavLink(adminTree, new AdminNavLink("Onramp Testing", new OnrampTesterWidget(adminService)));
    addNavLink(adminTree, new AdminNavLink("Tree Configuration", new TreeConfigAdminWidget(adminService)));
    // addNavLink(new AdminNavLink("Attributes", new AttributeAdminWidget(adminService)));

    return adminTree;
  }

  /**
   * @return the contentPanel
   */
  public SimplePanel getAdminContentPanel() {
    return adminContentPanel;
  }

  /**
   * @param contentPanel
   *          the contentPanel to set
   */
  public void setAdminContentPanel(SimplePanel contentPanel) {
    this.adminContentPanel = contentPanel;
  }

  private String getHeaderString(String text, AbstractImagePrototype image) {
    // Add the image and text to a horizontal panel
    HorizontalPanel hPanel = new HorizontalPanel();
    hPanel.setSpacing(0);
    hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    // hPanel.add(image.createImage());
    HTML headerText = new HTML(text);
    headerText.setStyleName("cw-StackPanelHeader");
    hPanel.add(headerText);

    // Return the HTML string for the panel
    return hPanel.getElement().getString();
  }

  private void addNavLink(Tree navTree, AdminNavLink link) {
    TreeItem item = navTree.addItem(link.getName());
    navLinkMap.put(link.getName(), link);
    treeItemToNav.put(item, link);
  }

  private void activateNav(AdminNavLink nav) {
    adminContentPanel.setWidget(nav.getComponent());
    nav.getComponent().onShow();
  }

  private class AdminNavLink {
    private String name;
    private AdminComponent component;

    public AdminNavLink(String name, AdminComponent component) {
      setName(name);
      setComponent(component);
    }

    /**
     * @return the component
     */
    public AdminComponent getComponent() {
      return component;
    }

    /**
     * @param component
     *          the component to set
     */
    public void setComponent(AdminComponent component) {
      this.component = component;
    }

    /**
     * @return the name
     */
    public String getName() {
      return name;
    }

    /**
     * @param name
     *          the name to set
     */
    public void setName(String name) {
      this.name = name;
    }
  }

  /**
   * @author josh
   * 
   */
  public class AdminTreeListener implements TreeListener {
    public void onTreeItemSelected(TreeItem item) {
      AdminNavLink navItem = treeItemToNav.get(item);
      if (navItem != null) {
        if (adminContentPanel.getWidget() == navItem.getComponent()) {
          return;
        }
        History.newItem(navItem.getName());
        activateNav(navItem);
      } else {
        adminContentPanel.setWidget(new HTML("&nbsp;"));
      }
    }

    public void onTreeItemStateChanged(TreeItem item) {
    }
  }

}
