package com.alertscape.pump.onramp.file;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.AlertSourceRepository;
import com.alertscape.common.model.AlertStatus;
import com.alertscape.common.model.severity.Severity;
import com.alertscape.common.model.severity.SeverityFactory;

public class BasicFileOnramp {

  // this class to read in from a file, and construct an alert from that text data
  // the text data in that file will contain structured text, in the following format
  // alertid*|shortdesc|description|severity|currenttimestamp|count*|status|majortags|minortags
  // where major tags and minor tags are passed in the format of:
  // major: |key=val, key=val, key=val, key=val|
  // minor: |key=val, key=val, key=val, key=val|
  // so all together, this would look like:
  // alertid*|description|severity|currenttimestamp*|count*|status|key=val,key=val|key=val,key=val
  //

  // the basicfileonramp will parse this information, according to a set of rules,
  // the rules will be the following, and must be configurable
  // the alertid can be provided in the file, or reconciled in the pump
  // the source id is configured in a config file, and will be inserted into the alert, it does not need to be in the
  // text file
  // which fields make up uniqueness: just a comma delimited list of the fields that make up uniqueness, probably major
  // and minor tag names, or base field names.
  // another option could be whether or not the text file with include an
  // current timestamp, or whether the current timestamp will be provided
  // automatically by the onramp
  // whether or not count will be included in the text file from an external
  // source, or automatically determined by the pump
  // it is assumed that the onramp will *not* maintain a state of all of the
  // alerts that it has standing, and therefore it will not be able to
  // determine the count value, or the firsttimestamp
  // it is assumed that if any kind of pre-existing state is required in order
  // to determine a value of an alert, then the onramp will pass those values
  // in as null to the pump, and the pump will need to calculate them: for
  // example, calculated alertid, calculated count, based on whether or not
  // the alert is already in the pump's consolidated alert memory
  // it is assumed that the onramp will use the unid configuration to populate
  // the unid field so that the pump can determine whether to inc the count if
  // necessary.
  // it is assumed that the pump will validate the alert when it comes in,
  // specifically with regard to count, and if there is no count specified,
  // then the pump will determine whether or not
  // TODO: will need to make sure that the process is capabile of reading a
  // file without locking it, so that the writer can continuously write to it
  // TODO: will need to make sure that the process is capable of clearing the
  // file out if necessary, so that the disk does not fill up with all of the
  // text that the file is filling with

  // TODO: change this so that it takes in the new fields that exist after you align to the alert object
  // shortdesc
  // severity as enum
  // status as enum
  // Major and Minor Tags as full fledged objects
  private static final ASLogger LOG = ASLogger.getLogger(BasicFileOnramp.class);

  // instance variables
  private String filePath; // the path to the file that will be continuously parsed
  private boolean useAlertId; // flag telling onramp whether or not to use the alertid value provided in the alert file
  private boolean useCount; // flag telling onramp whether or not to use the count value provided in the alert file
  private boolean useOccurTime; // flag telling onramp whether or not to use the occurTime value provided in the alert
  // file
  private String delimiter = "|"; // the delimiter between the main fields of an alert
  private String subdelimiter = ","; // the delimiter between the different key value pairs
  private String subsubdelimiter = "="; // the delimiter between a key and a value
  private String sourceName; // the source id for this fileonramp
  private AlertSourceRepository alertSourceRepository;

  // constructor
  public BasicFileOnramp() {

    useAlertId = true; // by default, assume that the taking an alertid from an external source
    useCount = true; // by default, assume that the onramp will be using the counts that it is provided.
    useOccurTime = false; // by default, assume that the onramp will determine the occur time itself
  }

  // TODO: need to refactor this out into a base interface for all onramps to use, this will be the method that the pump
  // calls on every onramp that it has, and it should return all of the alerts from this ramp to the pump
  public List<Alert> processOnRamp() {

    // create a data structure to hold the list of new alerts
    ArrayList<Alert> newAlerts = new ArrayList<Alert>(); // array of new alerts, which will be returns to the caller.
    // TODO: probably should reuse this array for performance reasons

    try {

      // read the file into a list of strings that represent lines in the
      // file
      List<String> newLines = readFileIntoList();

      // TODO: this might be a good place to truncate the file, or delete
      // it, or whatever.

      // iterate through the list of strings that has been created
      for (int i = 0; i < newLines.size(); i++) {

        // get the next line that was in the file
        String line = newLines.get(i);
        try {

          // parse the line, create an alert
          Alert alert = parseLineIntoAlert(line);

          // add the alert to the list of new alerts.
          newAlerts.add(alert);
        } catch (Throwable e) {

          LOG.error("Unable to parse alert: " + line, e);
        }

      }

      // clean up the list of strings
    } catch (Throwable e) {

      LOG.error("Unable to read file: " + filePath, e);
    }

    // debug statement
    if(LOG.isDebugEnabled()) {
      LOG.debug(newAlerts.toString());
    }
    return newAlerts;
  }

  private List<String> readFileIntoList() throws Throwable {

    List<String> newLines = new ArrayList<String>(); // array of new lines to be parsed out
    // TODO: should probably reuse the same array if possible, make it an instance variable, for performance reasons.

    // TODO: change all of this to use the nio classes, which are supposed to be much better, as I understand it, and
    // faster, and easier.
    // open the file
    BufferedReader reader = new BufferedReader(new java.io.FileReader(new File(filePath)));

    // read the file, line by line, until you get to the end.
    String nextLine = reader.readLine();
    while (nextLine != null) // the reader should return null if it is at
    // the end of the file
    {

      // create a string which represents the line
      newLines.add(nextLine);

      // add the string to an array of strings that represents the file
      nextLine = reader.readLine();
    }

    // close the reader and files
    reader.close();
    reader = null;
    nextLine = null;

    return newLines;
  }

  private Alert parseLineIntoAlert(String line) throws Throwable {

    // TODO: if I recall, the stringtokenizer is sort of inefficient, and there is probably a better way to do this in
    // the long term
    StringTokenizer tok = new StringTokenizer(line, delimiter, true); // set up to return delimiters also, so that empty
    // fields can be handled

    // create a new alert that we will now populate
    Alert a = new Alert();

    // set the source id of this alert to the value that is set for this
    // onramp
    AlertSource source = getAlertSourceRepository().getAlertSource(getSourceName());
    a.setSource(source);

    // the first token should be the alertid, depending on whether it should
    // be ignored, created, or not
    // determine whether the alert id needs to be created, or just taken
    // from the file
    String id = tok.nextToken();
    if (!isDelimiter(id, delimiter)) {

      if (useAlertId) {

        a.setAlertId(Long.parseLong(id));
      }

      tok.nextToken(); // eat the delimiter after this field
    }

    // note that the pump will need to detect that there was a null returned
    // for id, and therefore it must determine if the alert is in memory
    // already, depending on uniqueness, and then populate the alertid

    // the next token should be the short description
    String shortdesc = tok.nextToken();
    if (!isDelimiter(shortdesc, delimiter)) {

      a.setShortDescription(shortdesc);
      tok.nextToken(); // eat the delimiter after this field
    }

    // the next token should be the description
    String desc = tok.nextToken();
    if (!isDelimiter(desc, delimiter)) {

      a.setShortDescription(desc);
      tok.nextToken(); // eat the delimiter after this field
    }

    // the next token should be the severity
    String severity = tok.nextToken();
    if (!isDelimiter(severity, delimiter)) {

      int sevInt = Integer.parseInt(severity);
      Severity sev = SeverityFactory.getInstance().getSeverity(sevInt);
      a.setSeverity(sev);
      tok.nextToken(); // eat the delimiter after this field
    }

    // parse out the currenttimestamp of the alert
    String occurTimeString = tok.nextToken();
    if (!isDelimiter(occurTimeString, delimiter)) {

      // determine if we are creating a new date, and insert that, based
      // on
      // the configuration
      if (useOccurTime) {

        Date occurTimeDate = null;
        // TODO: need to parse this date, based on format, and then
        // create a
        // Date val
        a.setLastOccurence(occurTimeDate);
      } else {

        // we will just assume that the current system time is
        // appropriate
        Date occurTimeDate = new Date(); // automatically initialized to
        // current time
        a.setLastOccurence(occurTimeDate);
      }

      tok.nextToken(); // eat the delimiter after this field
    }

    // note that the pump will need to detect that there was a null returned
    // for firstdate, every time, and therefore it must determine if the
    // alert is in memory already, depending on uniqueness, and then
    // populate the alertid

    // the parse out the count of the alert, and check whether or not we
    // should use this value
    String count = tok.nextToken();
    if (!isDelimiter(count, delimiter)) {

      if (useCount) {

        a.setCount(Long.parseLong(count));
      }

      tok.nextToken(); // eat the delimiter after this field
    }

    // the next token should be the status
    String status = tok.nextToken();
    if (!isDelimiter(status, delimiter)) {

      a.setStatus(AlertStatus.STANDING);
      tok.nextToken(); // eat the delimiter after this field
    }

    // parse the major tags, and insert them into the alert
    String majorTags = tok.nextToken();
    if (!isDelimiter(majorTags, delimiter)) {

      StringTokenizer majorTok = new StringTokenizer(majorTags, subdelimiter);
      while (majorTok.hasMoreTokens()) {

        // get the next keyval pair
        String keyval = majorTok.nextToken();

        // now split the keyval into key and val
        StringTokenizer keyvalTok = new StringTokenizer(keyval, subsubdelimiter);
        if (keyvalTok.countTokens() == 2) {

          // parse out the key and the val
          String key = keyvalTok.nextToken();
          String val = keyvalTok.nextToken();

          // insert the key and the val into the alert
          a.addMajorTag(key, val);
        }

      }

      tok.nextToken(); // eat the delimiter after this field
    }

    // parse the minor tags, and insert them into the alert
    if (tok.hasMoreTokens()) {

      String minorTags = tok.nextToken();
      if (!isDelimiter(minorTags, delimiter)) {

        StringTokenizer minorTok = new StringTokenizer(minorTags, subdelimiter);
        while (minorTok.hasMoreTokens()) {

          // get the next keyval pair
          String keyval = minorTok.nextToken();

          // now split the keyval into key and val
          StringTokenizer keyvalTok = new StringTokenizer(keyval, subsubdelimiter);
          if (keyvalTok.countTokens() == 2) {

            // parse out the key and the val
            String key = keyvalTok.nextToken();
            String val = keyvalTok.nextToken();

            // insert the key and the val into the alert
            a.addMinorTag(key, val);
          }

        }

        // tok.nextToken(); // eat the delimiter after this field
      }

    }

    return a;
  }

  public String getFilePath() {

    return filePath;
  }

  public void setFilePath(String filePath) {

    this.filePath = filePath;
  }

  public boolean isUseAlertId() {

    return useAlertId;
  }

  public void setUseAlertId(boolean useAlertId) {

    this.useAlertId = useAlertId;
  }

  public boolean isUseCount() {

    return useCount;
  }

  public void setUseCount(boolean useCount) {

    this.useCount = useCount;
  }

  public boolean isUseOccurTime() {

    return useOccurTime;
  }

  public void setUseOccurTime(boolean useOccurTime) {

    this.useOccurTime = useOccurTime;
  }

  public String getDelimiter() {

    return delimiter;
  }

  public void setDelimiter(String delimiter) {

    this.delimiter = delimiter;
  }

  public String getSubdelimiter() {

    return subdelimiter;
  }

  public void setSubdelimiter(String subdelimiter) {

    this.subdelimiter = subdelimiter;
  }

  public String getSubsubdelimiter() {

    return subsubdelimiter;
  }

  public void setSubsubdelimiter(String subsubdelimiter) {

    this.subsubdelimiter = subsubdelimiter;
  }

  public String getSourceName() {

    return sourceName;
  }

  public void setSourceName(String sourceName) {

    this.sourceName = sourceName;
  }

  private boolean isDelimiter(String s, String delimiter) {

    return s.equals(delimiter);
  }

  // test harness main method...could be better than this, but oh well
  public static void main(String[] args) {

    // test harness
    BasicFileOnramp ramp = new BasicFileOnramp();
    ramp.setFilePath("C:/dev/eclipse/testfiles/rampinfile.txt");
    ramp.setSourceName("TEST");
    List<Alert> list = ramp.processOnRamp();
    LOG.debug(list);
    System.exit(0);
  }

  public AlertSourceRepository getAlertSourceRepository() {
    return alertSourceRepository;
  }

  public void setAlertSourceRepository(AlertSourceRepository alertSourceRepository) {
    this.alertSourceRepository = alertSourceRepository;
  }

}
