/**
 * 
 */
package com.alertscape.wizard.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.alertscape.wizard.client.InstallWizardInfo;
import com.alertscape.wizard.client.InstallWizardService;
import com.alertscape.wizard.client.WizardException;
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

  public boolean checkDirectory(String directory) {
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

  public void install(InstallWizardInfo info) throws WizardException {
    if (!info.isNoSchemaCreation()) {
      installSchema(info);
    }
    installFiles();
  }

  private void installFiles() {
    ServletContext servletContext = getServletConfig().getServletContext();
    String propertiesPath = servletContext.getRealPath("/WEB-INF/classes/alertscape.properties");
    String installedPath = servletContext.getRealPath("/WEB-INF/classes/alertscape.installed");

    File installed = new File(installedPath);
    try {
      installed.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void installSchema(InstallWizardInfo info) throws WizardException {
    Connection conn;
    try {
      conn = DriverManager.getConnection(info.getUrl(), info.getUsername(), info.getPassword());
    } catch (SQLException e) {
      e.printStackTrace();
      throw new WizardException("Couldn't get connection to create schema");
    }
    try {
      SqlRunner runner = new SqlRunner(conn);
      InputStream is = getClass().getResourceAsStream("/sql/mysql/schemaCreate.sql");
      InputStreamReader r = new InputStreamReader(is);
      runner.run(r);
    } catch (Exception e) {
      e.printStackTrace();
      throw new WizardException("Couldn't install schema: " + e.getLocalizedMessage());
    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException e) {
        }
      }
    }
  }

  public boolean doesAlertscapeSchemaExist(String driverName, String url, String username, String password)
      throws WizardException {
    Connection connection = null;
    ResultSet tables = null;

    try {
      Class.forName(driverName);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new WizardException("Couldn't find driver class: " + e.getLocalizedMessage());
    }
    try {
      connection = DriverManager.getConnection(url, username, password);
      tables = connection.getMetaData().getTables(null, null, "alerts", null);
      if (tables.next()) {
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new WizardException("Couldn't check for schema availability: " + e.getLocalizedMessage());
    } finally {
      if (tables != null) {
        try {
          tables.close();
        } catch (Exception e) {
        }
      }
      if (connection != null) {
        try {
          connection.close();
        } catch (Exception e) {
        }
      }
    }
  }

}
