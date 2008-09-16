/*
 * Created on Aug 27, 2006
 */
package com.alertscape.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author josh
 * @version $Version: $
 */
public class FormatHelper
{
  private static final SimpleDateFormat sdf = new SimpleDateFormat("M-d-yy H:m:s z");
  private FormatHelper()
  {
    // some text inserted here to test out svn via eclipse
  }
  
  public static String formatDate(Date d)
  {
    return sdf.format(d);
  }
}
