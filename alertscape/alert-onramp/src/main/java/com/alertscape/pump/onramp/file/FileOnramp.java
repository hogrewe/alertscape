/**
 * 
 */
package com.alertscape.pump.onramp.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.pump.onramp.AbstractPollingAlertOnramp;

/**
 * @author josh
 * 
 */
public abstract class FileOnramp extends AbstractPollingAlertOnramp {
  private static final ASLogger LOG = ASLogger.getLogger(FileOnramp.class);
  private String filename;
  private long currentLinePointer;
  private String currentLineHash;
  private String regex;
  private Pattern pattern;
  private MessageDigest m;
  private RandomAccessFile file;

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
    this.regex = regex;
    if (regex == null) {
      pattern = null;
    } else {
      pattern = Pattern.compile(regex);
    }
  }

  /**
   * @param line
   * @return
   */
  protected abstract Alert createAlert(Matcher matcher);

  @Override
  protected void setState(Object[] state) {
    if(state == null) {
      LOG.info("Couldn't find state for " + getSourceName());
      return;
    }
    currentLinePointer = (Long) state[0];
    currentLineHash = (String) state[1];
  }

  @Override
  protected void readUntilEnd() throws Exception {
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
        if (linesProcessed > 0) {
          long endTime = System.currentTimeMillis();
          long elapsed = endTime - startTime;
          if (elapsed < 1) {
            elapsed = 1;
          }
          long perSecond = (linesProcessed * 1000 / elapsed);
          LOG.info(getSourceName() + ", nothing left to process, done with " + linesProcessed + " lines in "
              + (endTime - startTime) + "ms at " + perSecond + "/s");
          writeState(currentLinePointer, currentLineHash);
        }
        return;
      }
      String nextLineHash = hashLine(line);
      Matcher matcher = pattern.matcher(line);
      if (!matcher.matches()) {
        continue;
      }
      Alert a = createAlert(matcher);
      if (a != null) {
        // LOG.debug(a.getShortDescription());
        sendAlert(a);
      }
      currentLinePointer = nextLinePointer;
      currentLineHash = nextLineHash;
      linesProcessed++;
      if (linesProcessed % 1000 == 0) {
        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;
        long perSecond = (linesProcessed * 1000 / elapsed);
        LOG.info(getSourceName() + " processed " + linesProcessed + " lines in " + (endTime - startTime) + "ms at "
            + perSecond + "/s");
        linesProcessed = 0;
        startTime = System.currentTimeMillis();
      }
    }
  }

  private RandomAccessFile openFile() throws FileNotFoundException {
    RandomAccessFile file = new RandomAccessFile(filename, "r");

    return file;
  }

  private boolean isFileLocationValid() throws IOException {
    if (currentLinePointer == -1) {
      return true;
    }

    if (file == null) {
      try {
        file = openFile();
      } catch (FileNotFoundException e) {
        LOG.error("Couldn't find the file to read in", e);
        return false;
      }
    }

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
  }

  private String hashLine(String line) {
    m.reset();
    m.update(line.getBytes(), 0, line.length());
    return new BigInteger(1, m.digest()).toString(16);
  }

}
