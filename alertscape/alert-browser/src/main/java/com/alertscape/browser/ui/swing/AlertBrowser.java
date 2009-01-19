/*
 * Created on Mar 21, 2006
 */
package com.alertscape.browser.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.jms.ConnectionFactory;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.BevelBorder;

import com.alertscape.AlertscapeException;
import com.alertscape.browser.common.auth.Authentication;
import com.alertscape.browser.common.auth.AuthenticationEvent;
import com.alertscape.browser.common.auth.AuthenticationListener;
import com.alertscape.browser.localramp.firstparty.preferences.LoadPreferencesAction;
import com.alertscape.browser.localramp.firstparty.preferences.SavePreferencesAction;
import com.alertscape.browser.localramp.firstparty.preferences.UserPreferencesPanel;
import com.alertscape.browser.model.BrowserContext;
import com.alertscape.browser.model.JmsAlertListener;
import com.alertscape.browser.ui.swing.panel.AlertBrowserStatusPanel;
import com.alertscape.browser.ui.swing.panel.collection.filter.TextFilterPanel;
import com.alertscape.browser.ui.swing.panel.collection.summary.AlertCollectionSummaryPanel;
import com.alertscape.browser.ui.swing.panel.collection.table.AlertCollectionTablePanel;
import com.alertscape.browser.ui.swing.panel.collection.tree.AlertTreePanel;
import com.alertscape.browser.ui.swing.panel.common.ASPanelBuilder;
import com.alertscape.browser.upramp.firstparty.ack.AcknowledgeAlertAction;
import com.alertscape.browser.upramp.firstparty.clear.ClearAlertAction;
import com.alertscape.browser.upramp.firstparty.customtag.CustomTagAction;
import com.alertscape.browser.upramp.firstparty.login.LoginAction;
import com.alertscape.browser.upramp.firstparty.mail.AlertMailAction;
import com.alertscape.browser.upramp.firstparty.predefinedtag.PredefinedTagAction;
import com.alertscape.browser.upramp.firstparty.unack.UnacknowledgeAlertAction;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertAttributeDefinition;
import com.alertscape.common.model.AlertCollection;
import com.alertscape.common.model.BinarySortAlertCollection;
import com.alertscape.service.AlertService;
import com.alertscape.service.AuthenticationService;
import com.alertscape.util.ImageFinder;

/**
 * @author josh
 * @version $Version: $
 */
public class AlertBrowser extends JFrame {
  private static final long serialVersionUID = 1L;
  private static final ASLogger LOG = ASLogger.getLogger(AlertBrowser.class);

  private AlertCollection collection;
  private ConnectionFactory jmsFactory;
  private static AlertService alertService;
  private AuthenticationService authenticationService;
  private static BrowserContext currentContext = new BrowserContext();
  private static AlertCollectionTablePanel tablePanel;
  private AlertTreePanel treePanel;

  public AlertBrowser() {
  }

  public void init() {
    try {
      LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
      for (LookAndFeelInfo info : installedLookAndFeels) {
        LOG.debug(info.getName() + ":" + info.getClassName());
      }
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      // UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
    } catch (ClassNotFoundException e) {
      LOG.error(e);
    } catch (InstantiationException e) {
      LOG.error(e);
    } catch (IllegalAccessException e) {
      LOG.error(e);
    } catch (UnsupportedLookAndFeelException e) {
      LOG.error(e);
    }
    setSize(800, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    treePanel = new AlertTreePanel();
    treePanel.init();
    
    collection = new BinarySortAlertCollection();
    
    AlertCollection treeCollection = treePanel.setMasterCollection(collection);

    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    TextFilterPanel filterPanel = new TextFilterPanel();
    AlertCollection filterCollection = filterPanel.setMasterCollection(treeCollection);
    AlertCollectionSummaryPanel summaryPanel = new AlertCollectionSummaryPanel();
    AlertCollection summaryCollection = summaryPanel.setMasterCollection(filterCollection);
    List<AlertAttributeDefinition> extendedAttributes = null;
    try {
      extendedAttributes = alertService.getAttributeDefinitions();
    } catch (AlertscapeException e) {
      LOG.error("Couldn't get attribute definitions", e);
    }
    tablePanel = new AlertCollectionTablePanel(summaryCollection, extendedAttributes);
    tablePanel.init();

    Icon bgImage = ImageFinder.getInstance().findImage("/com/alertscape/images/common/hdr_background_small.png");

    // Filter
    JPanel outerFilterPanel = new JPanel();
    outerFilterPanel.setBorder(BorderFactory.createTitledBorder("Quick Filter"));
    outerFilterPanel.setLayout(new GridLayout(1, 1));
    outerFilterPanel.add(filterPanel);

    // Summary
    JPanel outerSummaryPanel = new JPanel();
    outerSummaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
    outerSummaryPanel.setLayout(new GridLayout(1, 1));
    outerSummaryPanel.add(summaryPanel);

    // toolbar
    JToolBar actionToolbar = new JToolBar();
    actionToolbar.setOpaque(false);
    actionToolbar.setFloatable(false);

    AcknowledgeAlertAction ackAction = new AcknowledgeAlertAction();
    ackAction.setParentFrame(this);
    JButton ackButton = actionToolbar.add(ackAction);
    ackButton.setOpaque(false);

    UnacknowledgeAlertAction unackAction = new UnacknowledgeAlertAction();
    unackAction.setParentFrame(this);
    JButton unackButton = actionToolbar.add(unackAction);
    unackButton.setOpaque(false);
    
    ClearAlertAction clearAction = new ClearAlertAction();
    clearAction.setParentFrame(this);
    JButton clearButton = actionToolbar.add(clearAction);
    clearButton.setOpaque(false);    
    
    AlertMailAction mailAction = new AlertMailAction();
    mailAction.setParentFrame(this);
    JButton mailButton = actionToolbar.add(mailAction);
    mailButton.setOpaque(false);

    PredefinedTagAction predefinedTagAction = new PredefinedTagAction();
    predefinedTagAction.setParentFrame(this);
    JButton predefinedTagButton = actionToolbar.add(predefinedTagAction);
    predefinedTagButton.setOpaque(false);

    CustomTagAction customTagAction = new CustomTagAction();
    customTagAction.setParentFrame(this);
    JButton customTagButton = actionToolbar.add(customTagAction);
    customTagButton.setOpaque(false);
    
    LoginAction loginAction = new LoginAction();
    loginAction.setParentFrame(this);
    JButton loginButton = actionToolbar.add(loginAction);
    loginButton.setOpaque(false);

    // Table
    JPanel outerTablePanel = new JPanel();
    outerTablePanel.setLayout(new BorderLayout());
    JLabel hdrLabel = new JLabel();
    hdrLabel.setForeground(Color.white);
    hdrLabel.setText("Alerts");
    hdrLabel.setOpaque(false);
    Font f = new Font("Dialog", Font.ITALIC, 18);
    hdrLabel.setFont(f);
    JPanel headerPanel = new JPanel();
    headerPanel.setBorder(BorderFactory.createEmptyBorder(7, 15, 1, 3));
    headerPanel.setOpaque(false);
    BoxLayout hdrBox = new BoxLayout(headerPanel, BoxLayout.X_AXIS);
    headerPanel.setLayout(hdrBox);
    headerPanel.add(hdrLabel);
    headerPanel.add(Box.createHorizontalGlue());
    headerPanel.add(actionToolbar);

    JPanel imagePanel = ASPanelBuilder.wrapInBackgroundImage(headerPanel, bgImage);
    imagePanel.setBackground(Color.white);
    JPanel bgPanel = new JPanel();
    bgPanel.setLayout(new GridLayout(1, 1));
    bgPanel.add(imagePanel);
    bgPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    outerTablePanel.add(bgPanel, BorderLayout.NORTH);
    outerTablePanel.add(tablePanel, BorderLayout.CENTER);

    // North
    JPanel northPanel = new JPanel();
    northPanel.setLayout(new GridLayout(1, 2));
    northPanel.add(outerSummaryPanel);
    northPanel.add(outerFilterPanel);

    p.add(outerTablePanel, BorderLayout.CENTER);
//    p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    JPanel overallPane = new JPanel();
    overallPane.setLayout(new BorderLayout());
    overallPane.add(northPanel, BorderLayout.NORTH);
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel, p);
    overallPane.add(splitPane, BorderLayout.CENTER);
    overallPane.add(new AlertBrowserStatusPanel(), BorderLayout.SOUTH);
    setContentPane(overallPane);

    // create the menubar
    JMenuBar menubar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
    fileMenu.add(loginAction);

    JMenuItem exitItem = new JMenuItem("Exit", new ImageIcon(getClass().getResource(
        "/com/alertscape/images/mini/action_stop.gif")));
    exitItem.setMnemonic(KeyEvent.VK_X);
    exitItem.getAccessibleContext().setAccessibleDescription("Close AMP");
    exitItem.setToolTipText("Close AMP");
    fileMenu.add(exitItem);

    // other ideas for the file menu
    // - Switch user
    // - Logoff
    // - Export (to CSV, Excel)
    // - Print (selected alerts or all alerts in view)

    JMenu editMenu = new JMenu("Edit");
    // editMenu.setMnemonic(KeyEvent.VK_E);
    // ideas for the edit menu
    // - Copy
    // - Paste
    // - Cut
    // - Select All
    // - Find (search for alert: in the tree, in the table, in the history)
    // - window configurations (dock type stuff)

    JMenu actionsMenu = new JMenu("Actions");
    // actionsMenu.setMnemonic(KeyEvent.VK_A);
    actionsMenu.add(ackAction);
    actionsMenu.add(unackAction);
    actionsMenu.add(clearAction);
    actionsMenu.addSeparator();
    actionsMenu.add(mailAction);
    actionsMenu.add(predefinedTagAction);
    actionsMenu.add(customTagAction);
 
    // - email alerts
    // - quick tags:
    // - acknowledge alerts (acknowledged by field is just a tag)
    // - unacknowledge alerts (acknowledged by field is just a tag)
    // - assign alerts (name is just a tag)
    // - move alerts (folder is just a tag)
    // - view comments
    // - add comment
    // - view history
    // - view ticket
    // - clear/delete alerts
    // - tag alerts (major/minor?)

    
    JMenu preferencesMenu = new JMenu("Preferences");
    //preferencesMenu.add(ackAction);
    
    // build a list of all of the userpreferencepanels
    List <UserPreferencesPanel>panels = new ArrayList();
    panels.add(filterPanel);
    panels.add(tablePanel);
    panels.add(summaryPanel);
    
    SavePreferencesAction savePrefsAction = new SavePreferencesAction(panels);
    savePrefsAction.setParentFrame(this);
    preferencesMenu.add(savePrefsAction);
    LoadPreferencesAction loadPrefsAction = new LoadPreferencesAction(panels);
    loadPrefsAction.setParentFrame(this);
    preferencesMenu.add(loadPrefsAction);
    

    
    JMenu helpMenu = new JMenu("Help");
    // helpMenu.setMnemonic(KeyEvent.VK_H);
    // - About AMP
    // - Software Updates
    // - Report a bug or enhancement
    // - Release Notes
    // - Forums (Link to website Forums)
    // - User Guide (Link to versioned user guide doc online)

    menubar.add(fileMenu);
    menubar.add(editMenu);
    menubar.add(actionsMenu);
    menubar.add(preferencesMenu);
    menubar.add(helpMenu);

    this.setJMenuBar(menubar);

    
    // create the popupmenu for the table
    JPopupMenu popup = new JPopupMenu();
    popup.add(ackAction);
    popup.add(unackAction);
    popup.add(clearAction);
    popup.addSeparator();
    popup.add(mailAction);
    popup.add(predefinedTagAction);
    popup.add(customTagAction);
    tablePanel.setPopup(popup); 
    
    // jframe housekeeping
    setTitle("AMP - Alertscape Management Portal");
    URL cevImageUrl = getClass().getResource("/com/alertscape/images/common/as_logo2_32.png");
    ImageIcon cevImage = new ImageIcon(cevImageUrl);
    setIconImage(cevImage.getImage());
    setVisible(true);

    Authentication.addAuthenticationListener(new AuthenticationListener() {
      public void handleAuthEvent(AuthenticationEvent e) {
      	// TODO: there is probably a better way to do this than throwing it into a thread, but this is at least an improvement over locking the ui for 2 minutes while it DLs alerts...
      	Thread t = new Thread()
      	{
      		public void run()
      		{
      			initJms();
      		}
      	};
      	
      	t.start();
        
      }
    });

    
    // GenerateEvents gen = new GenerateEvents(collection);
    // int groupSize = 1000;
    // List<Alert> events = new ArrayList<Alert>(groupSize);
    // for (int i = 0; i < 30000; i++) {
    // events.add(gen.buildNewEvent());
    // if (i % groupSize == 0) {
    // ASLogger.debug(i);
    // collection.processAlerts(events);
    // events.clear();
    // }
    // }
    // collection.processAlerts(events);
    //
    // Thread t = new Thread(gen);
    // t.start();
  }

  private void initJms() {
    JmsAlertListener listener = new JmsAlertListener();
    listener.setCollection(collection);
    listener.setFactory(jmsFactory);

    try {
      listener.startListening();
      // GET ALL ALERTS
      List<Alert> alerts = alertService.getAllAlerts();
      LOG.debug("Retrieved " + alerts.size() + " open alerts, processing");
      collection.processAlerts(alerts);
      LOG.info("Processed " + alerts.size() + " open alerts");
      listener.startProcessing();
    } catch (AlertscapeException e) {
      JOptionPane.showMessageDialog(this, "Couldn't initalize alert listener", "Alert Listener Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * @return the jmsFactory
   */
  public ConnectionFactory getJmsFactory() {
    return jmsFactory;
  }

  /**
   * @param jmsFactory
   *          the jmsFactory to set
   */
  public void setJmsFactory(ConnectionFactory jmsFactory) {
    this.jmsFactory = jmsFactory;
  }

  /**
   * @return the alertService
   */
  public AlertService getAlertService() {
    return alertService;
  }

  /**
   * @param alertService
   *          the alertService to set
   */
  public void setAlertService(AlertService alertService) {
    AlertBrowser.alertService = alertService;
  }

  public static void setCurrentContext(BrowserContext currentContext) {
    AlertBrowser.currentContext = currentContext;
  }

  public static BrowserContext getCurrentContext() {
    List<Alert> curAlerts = tablePanel.getSelectedAlerts();
    currentContext.setSelectedAlerts(curAlerts);
    return currentContext;
  }

  // TODO: We have to get rid of these static methods
  public static void clear(List<Alert> alerts) {
    try {
      alertService.clear(Authentication.getUser("CEV"), alerts);
    } catch (AlertscapeException e) {
      LOG.error("Couldn't clear alerts", e);
    }
  }

  public static void acknowledge(List<Alert> alerts) {
    try {
      alertService.acknowledge(Authentication.getUser("CEV"), alerts);
    } catch (AlertscapeException e) {
      LOG.error("Couldn't acknowledge alerts", e);
    }
  }

  public static void unacknowledge(List<Alert> alerts) {
    try {
      alertService.unacknowledge(Authentication.getUser("CEV"), alerts);
    } catch (AlertscapeException e) {
      LOG.error("Couldn't acknowledge alerts", e);
    }
  }

  /**
   * @return the authenticationService
   */
  public AuthenticationService getAuthenticationService() {
    return authenticationService;
  }

  /**
   * @param authenticationService
   *          the authenticationService to set
   */
  public void setAuthenticationService(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
    // TODO: This whole static Authentication thing is a hack and should be taken out
    Authentication.setAuthenticationService(authenticationService);
  }
}
