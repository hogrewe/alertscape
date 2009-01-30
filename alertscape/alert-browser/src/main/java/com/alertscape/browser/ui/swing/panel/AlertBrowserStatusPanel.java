/*
 * Created on Aug 15, 2006
 */
package com.alertscape.browser.ui.swing.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.alertscape.browser.ui.swing.AlertBrowserStatus;
import com.alertscape.util.FormatHelper;

/**
 * @author josh
 * @version $Version: $
 */
public class AlertBrowserStatusPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private UserPanel userPanel;
  private MessagePanel messagePanel;
  private JLabel timeLabel;
  private Timer updateTimer;
  private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z");

  public AlertBrowserStatusPanel() {
    init();
  }

  protected void init() {
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    setLayout(new BorderLayout());
    this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

    //Border beveled = BorderFactory.createBevelBorder(BevelBorder.LOWERED);    
    //Border empty = BorderFactory.createEmptyBorder(2, 3, 2, 3);
    //Border statusBorder = BorderFactory.createCompoundBorder(beveled, empty);
    
    Border empty = BorderFactory.createEmptyBorder(2, 3, 2, 3);
    Border leftpipeBorder = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.lightGray), empty);
    Border rightpipeBorder = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.lightGray), empty);
    
    userPanel = new UserPanel();
    userPanel.setBorder(rightpipeBorder);
    messagePanel = new MessagePanel();
    messagePanel.setBorder(empty);
    AlertBrowserStatus.registerPanel(messagePanel);
    timeLabel = new JLabel();
    timeLabel.setBorder(leftpipeBorder);
    timeLabel.setFont(timeLabel.getFont().deriveFont(Font.BOLD));
    add(userPanel, BorderLayout.WEST);
    add(messagePanel, BorderLayout.CENTER);
    add(timeLabel, BorderLayout.EAST);

    updateTime();
    updateTimer = new Timer(1000, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        updateTime();
      }
    });
    updateTimer.start();
  }

  protected void updateTime() {
    timeLabel.setText(FormatHelper.formatDate(new Date()));
  }
}
