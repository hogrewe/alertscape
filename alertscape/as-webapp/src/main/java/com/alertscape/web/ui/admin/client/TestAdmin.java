/**
 * 
 */
package com.alertscape.web.ui.admin.client;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author josh
 * 
 */
public class TestAdmin extends Composite {
  public TestAdmin() {
    // Create a new stack panel
    DecoratedStackPanel stackPanel = new DecoratedStackPanel();
    stackPanel.setWidth("200px");

    // Add the Mail folders
    String mailHeader = getHeaderString("Mail", null);
    stackPanel.add(createMailItem(), mailHeader, true);

    // Add a list of filters
    String filtersHeader = getHeaderString("Filters", null);
    stackPanel.add(createFiltersItem(), filtersHeader, true);

    // Add a list of contacts
    String contactsHeader = getHeaderString("Contacts", null);
    stackPanel.add(createContactsItem(), contactsHeader, true);

    // Return the stack panel
    stackPanel.ensureDebugId("cwStackPanel");

    initWidget(stackPanel);
  }

  /**
   * Create the list of Contacts.
   * 
   * @param images
   *          the {@link Images} used in the Contacts
   * @return the list of contacts
   */
  private VerticalPanel createContactsItem() {
    // Create a popup to show the contact info when a contact is clicked
    HorizontalPanel contactPopupContainer = new HorizontalPanel();
    contactPopupContainer.setSpacing(5);
//    contactPopupContainer.add(images.defaultContact().createImage());
    final HTML contactInfo = new HTML();
    contactPopupContainer.add(contactInfo);
    final PopupPanel contactPopup = new PopupPanel(true, false);
    contactPopup.setWidget(contactPopupContainer);

    // Create the list of contacts
    VerticalPanel contactsPanel = new VerticalPanel();
    contactsPanel.setSpacing(4);
    String[] contactNames = {"Joe", "Bob", "Henri"};
    String[] contactEmails = {"Nope", "None", "Nada"};
    for (int i = 0; i < contactNames.length; i++) {
      final String contactName = contactNames[i];
      final String contactEmail = contactEmails[i];
      final HTML contactLink = new HTML("<a href=\"javascript:undefined;\">" + contactName + "</a>");
      contactsPanel.add(contactLink);

      // Open the contact info popup when the user clicks a contact
      contactLink.addClickListener(new ClickListener() {
        public void onClick(Widget w) {
          // Set the info about the contact
          contactInfo.setHTML(contactName + "<br><i>" + contactEmail + "</i>");

          // Show the popup of contact info
          int left = contactLink.getAbsoluteLeft() + 14;
          int top = contactLink.getAbsoluteTop() + 14;
          contactPopup.setPopupPosition(left, top);
          contactPopup.show();
        }
      });
    }
    return contactsPanel;
  }

  /**
   * Create the list of filters for the Filters item.
   * 
   * @return the list of filters
   */
  private VerticalPanel createFiltersItem() {
    VerticalPanel filtersPanel = new VerticalPanel();
    filtersPanel.setSpacing(4);
    for (String filter : new String[] {"All", "None"}) {
      filtersPanel.add(new CheckBox(filter));
    }
    return filtersPanel;
  }

  /**
   * Create the {@link Tree} of Mail options.
   * 
   * @param images
   *          the {@link Images} used in the Mail options
   * @return the {@link Tree} of mail options
   */
  private Tree createMailItem() {
    Tree mailPanel = new Tree();
    TreeItem mailPanelRoot = mailPanel.addItem("foo@example.com");
    String[] mailFolders = {"Inbox", "Outbox", "Outhouse"};
    mailPanelRoot.addItem(mailFolders[0]);
    mailPanelRoot.addItem(mailFolders[1]);
    mailPanelRoot.addItem(mailFolders[2]);
    mailPanelRoot.setState(true);
    return mailPanel;
  }

  /**
   * Get a string representation of the header that includes an image and some text.
   * 
   * @param text
   *          the header text
   * @param image
   *          the {@link AbstractImagePrototype} to add next to the header
   * @return the header as a string
   */
  private String getHeaderString(String text, AbstractImagePrototype image) {
    // Add the image and text to a horizontal panel
    HorizontalPanel hPanel = new HorizontalPanel();
    hPanel.setSpacing(0);
    hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//    hPanel.add(image.createImage());
    HTML headerText = new HTML(text);
    headerText.setStyleName("cw-StackPanelHeader");
    hPanel.add(headerText);

    // Return the HTML string for the panel
    return hPanel.getElement().getString();
  }

}
