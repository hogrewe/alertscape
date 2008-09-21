/*
 * Created on Mar 21, 2006
 */
package com.alertscape.browser.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.BevelBorder;

import com.alertscape.browser.common.auth.Authentication;
import com.alertscape.browser.ui.swing.panel.AlertBrowserStatusPanel;
import com.alertscape.browser.ui.swing.panel.collection.filter.TextFilterPanel;
import com.alertscape.browser.ui.swing.panel.collection.summary.AlertCollectionSummaryPanel;
import com.alertscape.browser.ui.swing.panel.collection.table.AlertCollectionTablePanel;
import com.alertscape.browser.ui.swing.panel.common.ASPanelBuilder;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;
import com.alertscape.common.model.IndexedAlertCollection;
import com.alertscape.tester.GenerateEvents;
import com.alertscape.util.ImageFinder;

/**
 * @author josh
 * @version $Version: $
 */
public class AlertBrowser extends JFrame {
  private static final long serialVersionUID = 1L;

  private AlertCollection collection;

  public AlertBrowser() {
    init();
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
    AlertCollectionTablePanel tablePanel = new AlertCollectionTablePanel(summaryCollection);

    Icon bgImage = ImageFinder.getInstance().findImage("/com/alertscape/images/common/hdr_background_small.png");
    
    // Filter
    JPanel outerFilterPanel = new JPanel();
    outerFilterPanel.setBorder(BorderFactory.createTitledBorder("Quick Filter"));
    outerFilterPanel.setLayout(new GridLayout(1,1));
    outerFilterPanel.add(filterPanel);

    // Summary
    JPanel outerSummaryPanel = new JPanel();
    outerSummaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
    outerSummaryPanel.setLayout(new GridLayout(1, 1));
    outerSummaryPanel.add(summaryPanel);
    
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
    headerPanel.setLayout(new GridLayout(1, 1));
    headerPanel.add(hdrLabel);

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
    northPanel.setLayout(new GridLayout(2,1));
    northPanel.add(outerFilterPanel);
    northPanel.add(outerSummaryPanel);
    
    p.add(northPanel, BorderLayout.NORTH);
    p.add(outerTablePanel, BorderLayout.CENTER);
    p.add(new AlertBrowserStatusPanel(), BorderLayout.SOUTH);
    setContentPane(p);

    Authentication.login("CEV", "john.doe", null);
    setTitle("AMP - Alertscape Management Portal");
    URL cevImageUrl = getClass().getResource("/com/alertscape/images/common/as_logo2_32.png");
    ImageIcon cevImage = new ImageIcon(cevImageUrl);
    setIconImage(cevImage.getImage());
    setVisible(true);

    GenerateEvents gen = new GenerateEvents(collection);
    int groupSize = 1000;
    List<Alert> events = new ArrayList<Alert>(groupSize);
    for (int i = 0; i < 30000; i++) {
      events.add(gen.buildNewEvent());
      if (i % groupSize == 0) {
        ASLogger.debug(i);
        collection.processAlerts(events);
        events.clear();
      }
    }
    collection.processAlerts(events);

    Thread t = new Thread(gen);
    t.start();
  }
}
