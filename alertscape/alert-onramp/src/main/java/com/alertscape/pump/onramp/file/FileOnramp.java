/**
 * 
 */
package com.alertscape.pump.onramp.file;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alertscape.AlertscapeException;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.equator.AlertEquator;
import com.alertscape.common.model.severity.Severity;
import com.alertscape.common.model.severity.SeverityFactory;
import com.alertscape.pump.onramp.AbstractPollingAlertOnramp;

/**
 * @author josh
 * 
 */
public class FileOnramp extends AbstractPollingAlertOnramp {
  private static final String LINE_HASH = "LINE_HASH";
  private static final String LINE_POINTER = "LINE_POINTER";
  private static final ASLogger LOG = ASLogger.getLogger(FileOnramp.class);
  private String filename;
  private long currentLinePointer;
  private String currentLineHash;
  private String regex;
  private Pattern pattern;
  private MessageDigest m;
  private RandomAccessFile file;
  private AlertLineProcessor lineProcessor;
  private List<String> uniqueFields;
  private String severityDeterminedField;
  private Map<Integer, List<String>> severityMappings;
  private Method severityFieldGetter;

  public FileOnramp() {
    try {
      m = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      LOG.error("Couldn't find MD5 digester", e);
    }
  }
  
  /**
   * @return the filename
   */
  public String getFilename() {
    return filename;
  }

  /**
   * 
   * @param filename
   *          the filename to set
   */
  public void setFilename(String filename) {
    this.filename = filename;
  }

  /**
   * @return the regex
   */
  public String getRegex() {
    return regex;
  }

  /**
   * @param regex
   *          the regex to set
   */
  public void setRegex(String regex) {
    if (regex == null) {
      this.regex = null;
      pattern = null;
    } else {
      this.regex = regex.trim();
      pattern = Pattern.compile(this.regex);
    }
  }

  @Override
  protected int readUntilEnd() throws Exception {
    int linesProcessed = 0;
    long startTime = System.currentTimeMillis();

    while (true) {
      if (!isFileLocationValid()) {
        LOG.debug("Truncated, starting over");
        file = openFile();
        currentLinePointer = -1;
      }
      long nextLinePointer = file.getFilePointer();
      String line = file.readLine();
      if (line == null) {
        file = openFile();
        if (linesProcessed > 0) {
          long endTime = System.currentTimeMillis();
          long elapsed = endTime - startTime;
          if (elapsed < 1) {
            elapsed = 1;
          }
          long perSecond = (linesProcessed * 1000 / elapsed);
          LOG.info(getSourceName() + ", nothing left to process, done with " + linesProcessed + " lines in "
              + (endTime - startTime) + "ms at " + perSecond + "/s");
        }
        saveState();
        return linesProcessed;
      }
      String nextLineHash = hashLine(line);
      currentLinePointer = nextLinePointer;
      currentLineHash = nextLineHash;
      state.put(LINE_POINTER, currentLinePointer);
      state.put(LINE_HASH, currentLineHash);
      Matcher matcher = pattern.matcher(line);
      if (!matcher.matches()) {
        continue;
      }
      Alert a = getLineProcessor().createAlert(matcher);
      a.setSeverity(determineSeverity(a));
      if (a != null) {
        // LOG.debug(a.getShortDescription());
        sendAlert(a);
      }
      linesProcessed++;
      if (linesProcessed % 1000 == 0) {
        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;
        long perSecond = (linesProcessed * 1000 / elapsed);
        LOG.info(getSourceName() + " processed " + linesProcessed + " lines in " + (endTime - startTime) + "ms at "
            + perSecond + "/s");
        linesProcessed = 0;
        startTime = System.currentTimeMillis();
        saveState();
      }
    }
  }

  private RandomAccessFile openFile() throws FileNotFoundException {
    if(file != null) {
      try {
        file.close();
      } catch (IOException e) {
        LOG.error("Couldn't close file", e);
      }
    }
    RandomAccessFile newFile = new RandomAccessFile(filename, "r");

    return newFile;
  }

  private boolean isFileLocationValid() throws IOException {
    if (file == null) {
      try {
        file = openFile();
      } catch (FileNotFoundException e) {
        LOG.error("Couldn't find the file to read in", e);
        return false;
      }
    }

    if (currentLinePointer == -1) {
      return true;
    }

    try {
      if (file == null || file.length() < currentLinePointer) {
        return false;
      }

      file.seek(currentLinePointer);

      String line = file.readLine();
      if (line == null) {
        return false;
      }
      String lineHash = hashLine(line);

      return lineHash.equals(currentLineHash);
    } catch (Exception e) {
      return false;
    }
  }

  private String hashLine(String line) {
    m.reset();
    m.update(line.getBytes(), 0, line.length());
    return new BigInteger(1, m.digest()).toString(16);
  }

  protected Severity determineSeverity(Alert a) {
    Severity s = SeverityFactory.getInstance().getSeverity(0);

    try {
      Object sevFieldValue = severityFieldGetter.invoke(a, (Object[]) null);
      for (Integer level : severityMappings.keySet()) {
        List<String> values = severityMappings.get(level);
        for (String value : values) {
          if (value != null && sevFieldValue != null && value.equals(sevFieldValue.toString())) {
            return SeverityFactory.getInstance().getSeverity(level);
          }
        }
      }
    } catch (Exception e) {
      LOG.error("Couldn't get field from " + severityFieldGetter.getName() + " to determine severity", e);
    }

    return s;
  }


  @Override
  protected void initState(Map<String, Object> state) {
    currentLinePointer = (Long) state.get(LINE_POINTER);
    currentLineHash = (String) state.get(LINE_HASH);
  }

  /**
   * @return the lineProcessor
   */
  public AlertLineProcessor getLineProcessor() {
    return lineProcessor;
  }

  /**
   * @param lineProcessor
   *          the lineProcessor to set
   */
  public void setLineProcessor(AlertLineProcessor lineProcessor) {
    this.lineProcessor = lineProcessor;
  }

  /**
   * @return the uniqueFields
   */
  public List<String> getUniqueFields() {
    return uniqueFields;
  }

  /**
   * @param uniqueFields
   *          the uniqueFields to set
   */
  public void setUniqueFields(List<String> uniqueFields) {
    this.uniqueFields = uniqueFields;
    if(uniqueFields == null) {
      setEquator(null);
    } else {
      try {
        setEquator(new AlertEquator(uniqueFields));
      } catch (AlertscapeException e) {
        LOG.error("Couldn't set eauator", e);
      }
    }
  }

  /**
   * @return the severityDeterminedField
   */
  public String getSeverityDeterminedField() {
    return severityDeterminedField;
  }

  /**
   * @param severityDeterminedField
   *          the severityDeterminedField to set
   */
  public void setSeverityDeterminedField(String severityDeterminedField) {
    this.severityDeterminedField = severityDeterminedField;
    if (severityDeterminedField == null) {
      severityFieldGetter = null;
      return;
    }
    try {
      PropertyDescriptor d = new PropertyDescriptor(getSeverityDeterminedField(), Alert.class);
      severityFieldGetter = d.getReadMethod();
    } catch (IntrospectionException e) {
      LOG.error("Couldn't make getter for " + getSeverityDeterminedField(), e);
    }

  }

  /**
   * @return the severityMappings
   */
  public Map<Integer, List<String>> getSeverityMappings() {
    return severityMappings;
  }

  /**
   * @param severityMappings
   *          the severityMappings to set
   */
  public void setSeverityMappings(Map<Integer, List<String>> severityMappings) {
    this.severityMappings = severityMappings;
  }

}
