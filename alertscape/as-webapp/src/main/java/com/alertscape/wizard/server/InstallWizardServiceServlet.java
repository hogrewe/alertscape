/**
 * 
 */
package com.alertscape.wizard.server;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.alertscape.wizard.client.InstallWizardService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author josh
 * 
 */
public class InstallWizardServiceServlet extends RemoteServiceServlet implements InstallWizardService {
  private static final long serialVersionUID = 9182193997987339126L;

  public String getServerUser() {
    return System.getProperty("user.name");
  }

  public Boolean checkDirectory(String directory) {
    File f = new File(directory);
    if (f.exists() && f.isDirectory() && f.canWrite()) {
      return true;
    }
    return false;
  }

  public String getContext() {
    HttpServletRequest request = getThreadLocalRequest();
    int serverPort = request.getServerPort();
    boolean defaultPort = (serverPort == 80 || serverPort == 443);
    String serverContext = request.getScheme() + "://" + request.getServerName()
        + (defaultPort ? "" : ":" + serverPort) + "/" + request.getContextPath();
    return serverContext;
  }
  
  public void writeInstallFiles() {
    ServletContext servletContext = getServletConfig().getServletContext();
    String slashPath = servletContext.getRealPath("/");
    String beansPath = servletContext.getRealPath("/WEB-INF/alertscape-beans.xml");
    
    System.out.println("slash: " + slashPath);
    System.out.println("beans: " + beansPath);
  }
}
