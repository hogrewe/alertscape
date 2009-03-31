/*
 * Created on Mar 15, 2006
 */
package com.alertscape.browser.ui.swing;

import javax.swing.SwingUtilities;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alertscape.common.logging.ASLogger;

/**
 * @author josh
 * @version $Version: $
 */
public class AlertBrowserMain {
  private static final ASLogger LOG = ASLogger.getLogger(AlertBrowserMain.class);

  /**
   * @param args
   */
  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Usage: AlertBrowserMain jmsurl remoteserviceurl");
    }

    System.setProperty("jms.url", args[0]);
    System.setProperty("remote.service.url", args[1]);

    final String[] contextPaths = new String[] { "browser-beans.xml" };
    try {
      SwingUtilities.invokeAndWait(new Runnable() {
        @Override
        public void run() {
          new ClassPathXmlApplicationContext(contextPaths);
        }
      });
    } catch (Exception e) {

      LOG.error("Couldn't initialize UI", e);
    }
  }
}
