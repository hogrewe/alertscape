/**
 * 
 */
package com.alertscape.web.ui;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.ContextLoaderServlet;

/**
 * @author josh
 *
 */
public class AlertscapeContextLoaderServlet extends ContextLoaderServlet {

  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    if(alreadyInstalled()) {
      return;
    } else {
      resp.sendRedirect("com.alertscape.wizard.InstallWizard/InstallWizard.html");
    }
  }

  @Override
  public void init() throws ServletException {
    if(alreadyInstalled()) {
      super.init();
    }
  }
  
  protected boolean alreadyInstalled() {
    URL installed = getClass().getResource("/alertscape.installed");

    return installed != null;
  }
}
