/**
 * 
 */
package com.alertscape.pump.onramp.file;

import java.util.regex.Matcher;

import com.alertscape.common.model.Alert;
import com.alertscape.common.model.severity.SeverityFactory;

/**
 * @author josh
 * 
 */
public class WebAccessLogFileOnramp extends FileOnramp {
  // private static ASLogger LOG = ASLogger.getLogger(WebAccessLogFileOnramp.class);
  private String siteName;

  public WebAccessLogFileOnramp() {
    // TODO: take this out
    setRegex("^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"([^\"]+)\"");
  }

  /**
   * @param line
   * @return
   */
  protected Alert createAlert(Matcher matcher) {
    // for(int i=0; i<matcher.groupCount(); i++) {
    // LOG.debug("Group " + i + ": " + matcher.group(i));
    // }
    Alert a = new Alert();
    // Request
    String returnCode = matcher.group(6);
    SeverityFactory factory = SeverityFactory.getInstance();
    if (returnCode.contains("200")) {
      a.setSeverity(factory.getSeverity(0));
    } else if (returnCode.contains("403")) {
      a.setSeverity(factory.getSeverity(2));
    } else if (returnCode.contains("503")) {
      a.setSeverity(factory.getSeverity(3));
    } else if (returnCode.contains("404")) {
      a.setSeverity(factory.getSeverity(4));
    } else if (returnCode.contains("500")) {
      a.setSeverity(factory.getSeverity(4));
    } else {
      a.setSeverity(factory.getSeverity(1));
    }
    a.setItem(matcher.group(5));
    a.setItemType("URL");
    a.setType("HTTP Request : " + returnCode);
    a.setItemManager(getSiteName());
    a.setItemManagerType("Website");
    a.setLongDescription("Request from " + matcher.group(1) + " for " + matcher.group(5) + " code: " + returnCode);
    return a;
  }

  /**
   * @return the siteName
   */
  public String getSiteName() {
    return siteName;
  }

  /**
   * @param siteName
   *          the siteName to set
   */
  public void setSiteName(String siteName) {
    this.siteName = siteName;
  }

}
