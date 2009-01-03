/**
 * 
 */
package com.alertscape.browser.ui.swing.panel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author josh
 * 
 */
public class MessagePanel extends JPanel {
  private static final long serialVersionUID = -885708420109247032L;
  private String message;
  private JLabel messageLabel;
  private Timer messageDeleteTimer;

  public MessagePanel() {
    init();
  }

  private void init() {
    messageLabel = new JLabel("");
    messageDeleteTimer = new Timer(10000, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setMessage(null);
      }
    });
    setLayout(new GridLayout(1, 1));
    add(messageLabel);
  }

  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message
   *          the message to set
   */
  public void setMessage(String message) {
    this.message = message;
    messageLabel.setText(message == null ? "" : message);
    synchronized (messageDeleteTimer) {
      if (messageDeleteTimer.isRunning()) {
        messageDeleteTimer.stop();
      }
      messageDeleteTimer.start();
    }
  }

}
