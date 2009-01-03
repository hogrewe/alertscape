/**
 * 
 */
package com.alertscape.tester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class LogFileAlertGenerator implements AlertGenerator {
  private static final ASLogger LOG = ASLogger.getLogger(LogFileAlertGenerator.class);
  private BufferedReader fileReader;

  /**
   * @param args
   */
  public static void main(String[] args) {
    LogFileAlertGenerator reader = new LogFileAlertGenerator();
    try {
      reader.readFile();
    } catch (IOException e) {
      LOG.error(e);
    }

  }

  public LogFileAlertGenerator() {
    InputStream is = getClass().getResourceAsStream("/traplog2.txt");
    InputStreamReader isr = new InputStreamReader(is);
    fileReader = new BufferedReader(isr);
  }

  public Alert readAlert() {
    String line;
    try {
      line = fileReader.readLine();
    } catch (IOException e) {
      throw new RuntimeException("Trouble reading next line of log", e);
    }
    if (line == null) {
      return null;
    }

    // Get rid of the SNMP raw info
    String[] split = line.split("[\\{\\}]");

    // Get rid of the time stamp and the TRAP log type
    String[] alertSplit = split[0].split("[ ]+", 4);

    if (alertSplit.length < 4) {
      LOG.debug("Problem: " + line);
      return null;
    }

    // LOG.debug(alertSplit[3]);

    // Get rid of the logged alert id (g5XXXXXXX)
    String[] split2 = alertSplit[3].split("-", 2);

    if (split2.length < 2) {
      LOG.debug("Problem: " + line);
      return null;
    }

    // LOG.debug(split2[1]);

    // We have the alert fields now, just split them up
    String[] split3 = split2[1].split(":+");

    String itemManager = split3[0];

    String item;
    String type;
    // If we have 3 colon separated fields, then we have a MIB ID in there, which means the second field is the item,
    // plus the MID ID, and the third field is the type

    // If we have only 2 fields, then the item and type are together and are separated by a space

    if (split3.length > 2) {
      item = split3[1].substring(0, split3[1].lastIndexOf(' ')).trim().intern();
      type = split3[2].trim().intern();
    } else {
      String itemType = split3[1].trim();
      int index = itemType.lastIndexOf(' ');
      item = itemType.substring(0, index).trim().intern();
      type = itemType.substring(index).trim().intern();
    }

    // Strip off the (1) or similar on the type
    int parenIndex = type.lastIndexOf('(');
    if (parenIndex > 0) {
      type = type.substring(0, parenIndex).trim().intern();
    }

    String description = "";
    // Take the description as the SNMP raw
    if (split.length > 1) {
      description = split[1];

      if (description.length() > 50) {
        description = description.substring(0, 50);
      }
      description.intern();
    }

    Alert a = new Alert();

    a.setItem(item);
    a.setItemManager(itemManager);
    a.setType(type);
    a.setLongDescription(description);

    return a;
  }

  private void readFile() throws IOException {
    while (readAlert() != null)
      ;
  }

}
