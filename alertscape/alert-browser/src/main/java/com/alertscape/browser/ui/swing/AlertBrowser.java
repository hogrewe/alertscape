/*
 * Created on Mar 21, 2006
 */
package com.alertscape.browser.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.List;

import javax.jms.ConnectionFactory;
import javax.swing.BorderFactory;
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
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.BevelBorder;

import com.alertscape.AlertscapeException;
import com.alertscape.browser.common.auth.Authentication;
import com.alertscape.browser.common.auth.User;
import com.alertscape.browser.model.BrowserContext;
import com.alertscape.browser.model.JmsAlertListener;
import com.alertscape.browser.ui.swing.panel.AlertBrowserStatusPanel;
import com.alertscape.browser.ui.swing.panel.collection.filter.TextFilterPanel;
import com.alertscape.browser.ui.swing.panel.collection.summary.AlertCollectionSummaryPanel;
import com.alertscape.browser.ui.swing.panel.collection.table.AlertCollectionTablePanel;
import com.alertscape.browser.ui.swing.panel.common.ASPanelBuilder;
import com.alertscape.browser.upramp.firstparty.login.LoginAction;
import com.alertscape.browser.upramp.firstparty.mail.AlertMailAction;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;
import com.alertscape.common.model.IndexedAlertCollection;
import com.alertscape.service.AlertService;
import com.alertscape.util.ImageFinder;

/**
 * @author josh
 * @version $Version: $
 */
public class AlertBrowser extends JFrame {
  private static final long serialVersionUID = 1L;

  private AlertCollection collection;
  private ConnectionFactory jmsFactory;
  private AlertService alertService;
  private static BrowserContext currentContext = new BrowserContext();
  private static AlertCollectionTablePanel tablePanel;

  public AlertBrowser() {
  }

  public void init() {
    try {
      LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
      for (LookAndFeelInfo info : installedLookAndFeels) {
        ASLogger.debug(info.getName() + ":" + info.getClassName());
      }
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      // UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnsupportedLookAndFeelException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    setSize(800, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    collection = new IndexedAlertCollection();
    
    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    TextFilterPanel filterPanel = new TextFilterPanel();
    AlertCollection filterCollection = filterPanel.setMasterCollection(collection);
    AlertCollectionSummaryPanel summaryPanel = new AlertCollectionSummaryPanel();
    AlertCollection summaryCollection = summaryPanel.setMasterCollection(filterCollection);
    tablePanel = new AlertCollectionTablePanel(summaryCollection);

    Icon bgImage = ImageFinder.getInstance().findImage("/com/alertscape/images/common/hdr_background_small.png");

    // Filter
    JPanel outerFilterPanel = new JPanel();
    outerFilterPanel.setBorder(BorderFactory.createTitledBorder("Quick Filter"));
    //outerFilterPanel.setLayout(new GridLayout(1, 1));
    //outerFilterPanel.add(filterPanel);
    outerFilterPanel.setLayout(new GridLayout(1,1));
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
    
    LoginAction loginAction = new LoginAction();
    loginAction.setParentFrame(this);
    JButton loginButton = actionToolbar.add(loginAction);
    loginButton.setOpaque(false);
    
    AlertMailAction mailAction = new AlertMailAction();
    mailAction.setParentFrame(this);
    JButton mailButton = actionToolbar.add(mailAction);
    mailButton.setOpaque(false);
    
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
    headerPanel.setLayout(new GridLayout(1, 2));
    headerPanel.add(hdrLabel);
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

    p.add(northPanel, BorderLayout.NORTH);
    p.add(outerTablePanel, BorderLayout.CENTER);
    p.add(new AlertBrowserStatusPanel(), BorderLayout.SOUTH);
    setContentPane(p);

    User jd = Authentication.login("CEV", "john.doe", null);
    
    // TODO: remove this demo hack
    currentContext.setCurrentUser(jd);
    
    // create the menubar
    JMenuBar menubar = new JMenuBar();
    
    JMenu fileMenu = new JMenu("File");
    //fileMenu.setMnemonic(KeyEvent.VK_F);
    
    fileMenu.add(loginAction);
    
//    JMenuItem loginItem = new JMenuItem("Switch User", new ImageIcon(getClass().getResource("/com/alertscape/images/mini/icon_user.gif"))); 
//    loginItem.setMnemonic(KeyEvent.VK_U);		
//    loginItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.ALT_MASK));
//    loginItem.getAccessibleContext().setAccessibleDescription("Log a new user into Alertscape");
//    loginItem.setToolTipText("Log a new user into Alertscape");
//    fileMenu.add(loginItem);

    JMenuItem exitItem = new JMenuItem("Exit", new ImageIcon(getClass().getResource("/com/alertscape/images/mini/action_stop.gif"))); 
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
    //editMenu.setMnemonic(KeyEvent.VK_E);
    // ideas for the edit menu
    // - Copy
    // - Paste
    // - Cut
    // - Select All
    // - Find (search for alert: in the tree, in the table, in the history)
    // - window configurations (dock type stuff)
    
    JMenu actionsMenu = new JMenu("Actions");
    //actionsMenu.setMnemonic(KeyEvent.VK_A);
    actionsMenu.add(mailAction);
    
    // - email alerts
    // - quick tags:
    //   - acknowledge alerts (acknowledged by field is just a tag)
    //   - unacknowledge alerts (acknowledged by field is just a tag)
    //   - assign alerts (name is just a tag)
    //   - move alerts (folder is just a tag)
    // - view comments
    // - add comment
    // - view history
    // - view ticket
    // - clear/delete alerts
    // - tag alerts (major/minor?)
    
    JMenu helpMenu = new JMenu("Help");
    //helpMenu.setMnemonic(KeyEvent.VK_H);
    // - About AMP
    // - Software Updates
    // - Report a bug or enhancement
    // - Release Notes
    // - Forums (Link to website Forums)
    // - User Guide (Link to versioned user guide doc online)
    
    menubar.add(fileMenu);
    menubar.add(editMenu);
    menubar.add(actionsMenu);
    menubar.add(helpMenu);    
    
    this.setJMenuBar(menubar);
    
    
    
    
    setTitle("AMP - Alertscape Management Portal");
    URL cevImageUrl = getClass().getResource("/com/alertscape/images/common/as_logo2_32.png");
    ImageIcon cevImage = new ImageIcon(cevImageUrl);
    setIconImage(cevImage.getImage());
    setVisible(true);

    initJms();

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
      collection.processAlerts(alerts);
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
   * @param jmsFactory the jmsFactory to set
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
   * @param alertService the alertService to set
   */
  public void setAlertService(AlertService alertService) {
    this.alertService = alertService;
  }

public static void setCurrentContext(BrowserContext currentContext)
{
	AlertBrowser.currentContext = currentContext;
}

public static BrowserContext getCurrentContext()
{
	List<Alert> curAlerts = tablePanel.getSelectedAlerts();
	currentContext.setSelectedAlerts(curAlerts);
	return currentContext;
}
}
