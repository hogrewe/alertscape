/**
 * 
 */
package com.alertscape.wizard.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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

  public String getServerName() {
    HttpServletRequest request = getThreadLocalRequest();

    return request.getServerName();
  }

  public void install(InstallWizardInfo info) throws WizardException {
    if (!info.isNoSchemaCreation()) {
      installSchema(info);
    }
    installFiles(info);
  }

  private void installFiles(InstallWizardInfo info) throws WizardException {
    ServletContext servletContext = getServletConfig().getServletContext();
    String propertiesPath = servletContext.getRealPath("/WEB-INF/classes/alertscape.properties");
    String jnlpPath = servletContext.getRealPath("/webstart/alertbrowser.jnlp");
    String installedPath = servletContext.getRealPath("/WEB-INF/classes/alertscape.installed");

    FileWriter propsWriter = null;
    try {
      propsWriter = new FileWriter(propertiesPath);
      propsWriter.write("jdbc.driverClassName=" + info.getDriverName() + "\n");
      propsWriter.write("jdbc.url=" + info.getDbUrl() + "\n");
      propsWriter.write("jdbc.username=" + info.getUsername() + "\n");
      propsWriter.write("jdbc.password=" + info.getPassword() + "\n");
      propsWriter.write("jms.url=" + info.getJmsUrl() + "\n");
      propsWriter.write("ui.hostname=" + info.getContext() + "\n");
    } catch (IOException e) {
      e.printStackTrace();
      throw new WizardException("Couldn't write properties file: " + e.getLocalizedMessage());
    } finally {
      if (propsWriter != null) {
        try {
          propsWriter.close();
        } catch (IOException e) {
        }
      }
    }

    StringBuilder paramBuilder = new StringBuilder();
    paramBuilder.append("<argument>" + info.getJmsUrl() + "</argument>");
    paramBuilder.append("<argument>" + info.getContext() + "</argument>");

    String params = paramBuilder.toString();
    FileReader jnlpReader = null;
    FileWriter jnlpWriter = null;
    try {
      jnlpReader = new FileReader(jnlpPath);
      BufferedReader br = new BufferedReader(jnlpReader);
      StringBuilder jnlp = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) {
        jnlp.append(line);
        jnlp.append("\n");
      }
      String jnlpString = jnlp.toString();
      jnlpString = jnlpString.replaceAll("\\$params", params);
      jnlpString = jnlpString.replaceAll("\\$\\$codebase", info.getContext() + "/webstart");

      jnlpWriter = new FileWriter(jnlpPath);
      jnlpWriter.write(jnlpString);
    } catch (IOException e) {
      e.printStackTrace();
      throw new WizardException("Couldn't rewrite jnlp file: " + e.getLocalizedMessage());
    } finally {
      if (jnlpReader != null) {
        try {
          jnlpReader.close();
        } catch (IOException e) {
        }
      }
      if (jnlpWriter != null) {
        try {
          jnlpWriter.close();
        } catch (IOException e) {
        }
      }
    }
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
      conn = DriverManager.getConnection(info.getDbUrl(), info.getUsername(), info.getPassword());
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
