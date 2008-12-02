/*
 * Created on Mar 15, 2006
 */
package com.alertscape.browser.ui.swing;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author josh
 * @version $Version: $
 */
public class AlertBrowserMain {
  /**
   * @param args
   */
  public static void main(String[] args) {
    // new AlertBrowser();

    String[] contextPaths = new String[] { "browser-beans.xml" };
    new ClassPathXmlApplicationContext(contextPaths);
  }
}
