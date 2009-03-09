/**
 * 
 */
package com.alertscape.wizard.server;

import java.io.File;

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
    if(f.exists() && f.isDirectory() && f.canWrite()) {
      
      return true;
    }
    return false;
  }

}
