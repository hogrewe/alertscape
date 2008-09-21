/*
 * Created on Aug 15, 2006
 */
package com.alertscape.browser.ui.swing.panel;

import java.awt.BorderLayout;
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

/**
 * @author josh
 * @version $Version: $
 */
public class AlertBrowserStatusPanel extends JPanel
{
  private static final long serialVersionUID = 1L;

  private UserPanel userPanel;
  private JLabel timeLabel;
  private Timer updateTimer;
  private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z");

  public AlertBrowserStatusPanel( )
  {
    init( );
  }

  protected void init( )
  {
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    setLayout(new BorderLayout( ));

    Border beveled = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
    Border empty = BorderFactory.createEmptyBorder(2, 3, 2, 3);
    Border statusBorder = BorderFactory.createCompoundBorder(beveled, empty);
    userPanel = new UserPanel( );
    userPanel.setBorder(statusBorder);
    timeLabel = new JLabel( );
    timeLabel.setBorder(statusBorder);
    timeLabel.setFont(timeLabel.getFont( ).deriveFont(Font.BOLD));
    add(userPanel, BorderLayout.CENTER);
    add(timeLabel, BorderLayout.EAST);

    updateTime( );
    updateTimer = new Timer(1000, new ActionListener( )
    {
      public void actionPerformed(ActionEvent e)
      {
        updateTime( );
      }
    });
    updateTimer.start( );
  }

  protected void updateTime( )
  {
    timeLabel.setText(sdf.format(new Date( )));
  }
}
