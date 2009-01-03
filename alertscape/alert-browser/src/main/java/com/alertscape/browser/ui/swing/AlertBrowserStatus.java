/**
 * 
 */
package com.alertscape.browser.ui.swing;

import java.util.HashSet;
import java.util.Set;

import com.alertscape.browser.ui.swing.panel.MessagePanel;

/**
 * @author josh
 * 
 */
public class AlertBrowserStatus {
  private static Set<MessagePanel> panels = new HashSet<MessagePanel>();

  public static void statusChange(String message) {
    for (MessagePanel panel : panels) {
      panel.setMessage(message);
    }
  }

  public static void registerPanel(MessagePanel panel) {
    panels.add(panel);
  }
}
