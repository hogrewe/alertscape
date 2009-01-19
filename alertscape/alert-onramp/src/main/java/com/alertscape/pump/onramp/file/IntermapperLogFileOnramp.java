/**
 * 
 */
package com.alertscape.pump.onramp.file;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.severity.SeverityFactory;

/**
 * @author josh
 * 
 */
public class IntermapperLogFileOnramp extends FileOnramp {
  private static final ASLogger LOG = ASLogger.getLogger(IntermapperLogFileOnramp.class);
  private static final String PRIMARY_REGEX = "^([\\w\\/:]*)[\\W]*([\\w\\/:]*)[\\W]*(\\w*)[\\W]*(.*)";
  private static final String TRAP_KNOWN_REGEX = "^([\\w- ]*):([\\w]*)\\W*([\\w-]*\\W*[:]{1,2})*\\W*([\\w]*)";
  private static final String TRAP_UNKNOWN_REGEX = "^([\\w\\. \\(\\)]*)\\W*:{0,2}\\W*(\\w*).*";
  private static final String LINK_UTIL_REGEX = "^(util)\\W*(>=|<)\\W*(\\d*)\\W*\\([\\W\\d\\.%]*\\)\\W*:\\W*\\[[0-9]*\\]\\W*(.*)";
  private static final String LINK_STATUS_REGEX = "^(\\w*)\\W*:\\W*\\[\\d*\\]\\W*(.*)";
  private static final String WARN_REGEX = "^([\\w\\W]*\\w)\\W*::\\W*(.*)";
  private static final String CRIT_ENV_REGEX = "^(\\w*-Environmentals)\\W*:\\W*(\\w*)\\W*:(\\w*)\\W*\"(\\w*)\"";
  private static final String ETC_REGEX = "^(.*)::\\W*(.*)";

  private List<Set<String>> severityTypes = new ArrayList<Set<String>>();

  private Pattern trapKnownPattern;
  private Pattern trapUnknownPattern;
  private Pattern linkUtilPattern;
  private Pattern linkStatusPattern;
  private Pattern warnPattern;
  private Pattern critEnvPattern;
  private Pattern etcPattern;

  public IntermapperLogFileOnramp() {
    setRegex(PRIMARY_REGEX);
    trapKnownPattern = Pattern.compile(TRAP_KNOWN_REGEX);
    trapUnknownPattern = Pattern.compile(TRAP_UNKNOWN_REGEX);
    linkUtilPattern = Pattern.compile(LINK_UTIL_REGEX);
    linkStatusPattern = Pattern.compile(LINK_STATUS_REGEX);
    warnPattern = Pattern.compile(WARN_REGEX);
    critEnvPattern = Pattern.compile(CRIT_ENV_REGEX);
    etcPattern = Pattern.compile(ETC_REGEX);

    Set<String> normalTypes = new HashSet<String>();

    severityTypes.add(normalTypes);

    Set<String> warningTypes = new HashSet<String>();

    severityTypes.add(warningTypes);

    Set<String> minorTypes = new HashSet<String>();
    minorTypes.add("util >= 50");

    severityTypes.add(minorTypes);

    Set<String> majorTypes = new HashSet<String>();
    majorTypes.add("util >= 90");

    severityTypes.add(majorTypes);

    Set<String> critTypes = new HashSet<String>();
    critTypes.add("LINK DOWN");

    severityTypes.add(critTypes);
  }

  @Override
  protected Alert createAlert(Matcher matcher) {
    Alert a = null;

    String eventType = matcher.group(3);

    String remaining = matcher.group(4);
    if ("trap".equalsIgnoreCase(eventType)) {
      a = handleTrap(remaining);
    } else if ("link".equalsIgnoreCase(eventType)) {
      a = handleLink(remaining);
    } else if ("warn".equalsIgnoreCase(eventType)) {
      a = handleWarn(remaining);
    } else if ("crit".equalsIgnoreCase(eventType)) {
      a = handleCrit(remaining);
    } else if ("alrm".equalsIgnoreCase(eventType)) {
      a = handleAlarm(remaining);
    } else if ("okay".equalsIgnoreCase(eventType)) {
      // ignore
      // LOG.debug(eventType + ":::" + matcher.group(4));
    } else if ("dbug".equalsIgnoreCase(eventType)) {
      // LOG.debug(eventType + ":::" + matcher.group(4));
    } else {
      // LOG.debug(eventType + ":::" + remaining);
    }

    if (a == null) {
      return null;
    }

    SeverityFactory severityFactory = SeverityFactory.getInstance();

    if (a.getSeverity() == null) {
      a.setSeverity(severityFactory.getSeverity(0));
    }
    for (int i = 1; i < severityFactory.getNumSeverities(); i++) {
      if (severityTypes.get(i).contains(a.getType())) {
        a.setSeverity(severityFactory.getSeverity(i));
      }
    }
    
    if(a.getType().length() > 200) {
      a.setLongDescription(a.getType());
      a.setType(a.getType().substring(0,200));
    }

    return a;
  }

  /**
   * @param remaining
   * @return
   */
  private Alert handleAlarm(String remaining) {
    Alert a = null;

    Matcher etcMatcher = etcPattern.matcher(remaining);

    if (etcMatcher.matches()) {
      a = new Alert();

      String item = etcMatcher.group(1);
      String type = etcMatcher.group(2);

      a.setItem(item);
      a.setType(type);

      a.setSeverity(SeverityFactory.getInstance().getSeverity(2));
    } else {
      LOG.debug(remaining);
    }
    return a;
  }

  /**
   * @param remaining
   * @return
   */
  private Alert handleCrit(String remaining) {
    Alert a = null;

    Matcher envMatcher = critEnvPattern.matcher(remaining);
    Matcher etcMatcher = etcPattern.matcher(remaining);

    if (envMatcher.matches()) {
      a = new Alert();

      String itemManager = envMatcher.group(1);
      String item = envMatcher.group(2);
      String type = envMatcher.group(3);
      String description = envMatcher.group(4);

      a.setItem(item);
      a.setItemManager(itemManager);
      a.setType(type);
      a.setLongDescription(description);

      a.setSeverity(SeverityFactory.getInstance().getSeverity(4));
    } else if (etcMatcher.matches()) {
      a = new Alert();

      String item = etcMatcher.group(1);
      String type = etcMatcher.group(2);

      if (type.length() > 50) {
        a.setLongDescription(type);
        type = type.substring(0, 50);
      }

      a.setItem(item);
      a.setType(type);

      a.setSeverity(SeverityFactory.getInstance().getSeverity(4));
    } else {
      LOG.debug(remaining);
    }

    return a;
  }

  /**
   * @param remaining
   * @return
   */
  private Alert handleWarn(String remaining) {
    Alert a = null;

    Matcher warnMatcher = warnPattern.matcher(remaining);

    if (warnMatcher.matches()) {
      a = new Alert();

      String item = warnMatcher.group(1);
      String type = warnMatcher.group(2);

      a.setSeverity(SeverityFactory.getInstance().getSeverity(1));

      a.setItem(item);
      a.setType(type);
    } else {
      LOG.debug(remaining);
    }
    return a;
  }

  /**
   * @param remaining
   * @return
   */
  private Alert handleLink(String remaining) {
    Alert a = null;

    Matcher utilMatcher = linkUtilPattern.matcher(remaining);
    Matcher statusMatcher = linkStatusPattern.matcher(remaining);

    if (utilMatcher.matches()) {
      a = new Alert();
      String type = utilMatcher.group(1) + " " + utilMatcher.group(2) + " " + utilMatcher.group(3);
      String item = utilMatcher.group(4);

      a.setType(type);
      a.setItem(item);
    } else if (statusMatcher.matches()) {
      a = new Alert();
      String type = "LINK " + statusMatcher.group(1);
      String item = statusMatcher.group(2);

      a.setType(type);
      a.setItem(item);
    } else {
      LOG.debug(remaining);
    }

    return a;
  }

  /**
   * @param remaining
   * @return
   */
  private Alert handleTrap(String remaining) {
    Alert a = null;
    Matcher knownMatcher = trapKnownPattern.matcher(remaining);
    Matcher unknownMatcher = trapUnknownPattern.matcher(remaining);
    if (knownMatcher.matches()) {
      a = new Alert();
      String containment = knownMatcher.group(1);
      String item = knownMatcher.group(2);
      String type = knownMatcher.group(4);
      a.setItemManager(containment);
      a.setItem(item);
      a.setType(type);
    } else if (unknownMatcher.matches()) {
      a = new Alert();
      String item = unknownMatcher.group(1);
      String type = unknownMatcher.group(2);
      a.setItem(item);
      a.setType(type);
    } else {
      LOG.debug(remaining);
    }

    return a;
  }
}
