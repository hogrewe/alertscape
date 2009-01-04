/*
 * Created on Aug 27, 2006
 */
package com.alertscape.util;

import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.time.FastDateFormat;

/**
 * @author josh
 * @version $Version: $
 */
public class FormatHelper {
  /**
   * 
   */
  private static final FastDateFormat FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss z", TimeZone
      .getTimeZone("GMT"));

  private FormatHelper() {
    // some text inserted here to test out svn via eclipse
  }

  public static String formatDate(Date d) {
    return FORMAT.format(d);
  }
}
