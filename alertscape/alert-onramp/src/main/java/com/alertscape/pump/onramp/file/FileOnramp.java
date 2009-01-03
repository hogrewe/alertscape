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
import com.alertscape.common.model.severity.SeverityFactory;
import com.alertscape.pump.onramp.AlertOnramp;

/**
 * @author josh
 * 
 */
public class FileOnramp extends AlertOnramp{
  private static ASLogger LOG = ASLogger.getLogger(FileOnramp.class);
  private long sleepTimeBetweenReads = 10;
  private String filename;
  private long currentLinePointer;
  private String currentLineHash;
  private FileReadingRunner runner;
  private String regex;
  private Pattern pattern;

  public void init() {
    runner = new FileReadingRunner();
   
    // TODO: take this out
    setRegex("^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"([^\"]+)\"");

    Thread t = new Thread(runner);
    t.start();
  }

  public void shutdown() {
    runner.setStopped(true);
  }

  /**
   * @return the sleepTimeBetweenReads
   */
  public long getSleepTimeBetweenReads() {
    return sleepTimeBetweenReads;
  }

  /**
   * @param sleepTimeBetweenReads
   *          the sleepTimeBetweenReads to set
   */
  public void setSleepTimeBetweenReads(long sleepTimeBetweenReads) {
    this.sleepTimeBetweenReads = sleepTimeBetweenReads;
  }

  /**
   * @return the filename
   */
  public String getFilename() {
    return filename;
  }
  /**

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
   * @param regex the regex to set
   */
  public void setRegex(String regex) {
    this.regex = regex;
    if(regex == null) {
      pattern = null;
    } else {
      pattern = Pattern.compile(regex);
    }
  }

  private class FileReadingRunner implements Runnable {
    private boolean stopped;
    private MessageDigest m;
    private RandomAccessFile file;

    public FileReadingRunner() {
      try {
        m = MessageDigest.getInstance("MD5");
      } catch (NoSuchAlgorithmException e) {
        LOG.error("Couldn't find MD5 digester", e);
      }
    }

    /**
     * @return the stopped
     */
    public boolean isStopped() {
      return stopped;
    }

    /**
     * @param stopped
     *          the stopped to set
     */
    public void setStopped(boolean stopped) {
      synchronized (this) {
        this.stopped = stopped;
      }
      notifyAll();
    }

    public void run() {
      try {
        file = openFile();
      } catch (FileNotFoundException e) {
        LOG.error("Couldn't find the file to read in", e);
      }

      while (!stopped) {
        try {
          processLinesUntilEnd();
        } catch (FileNotFoundException e) {
          LOG.error("Couldn't find the file to read", e);
        } catch (IOException e) {
          LOG.error(e);
        }
        try {
          synchronized (this) {
            wait(sleepTimeBetweenReads);
          }
        } catch (InterruptedException e) {
        }
      }
    }

    private void processLinesUntilEnd() throws FileNotFoundException, IOException {
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
          if(linesProcessed > 0) {
            long endTime = System.currentTimeMillis();
            long elapsed = endTime - startTime;
            if(elapsed < 1) {
              elapsed = 1;
            }
            long perSecond = (linesProcessed*1000/elapsed);
            LOG.debug("Nothing left to process, done with " + linesProcessed + " lines in " + (endTime - startTime) + "ms at " + perSecond + "/s");
          }
          return;
        }
        String nextLineHash = hashLine(line);
        Alert a = createAlertFromLine(line);
        if(a != null) {
//          LOG.debug(a.getShortDescription());
          sendAlert(a);
        }
        currentLinePointer = nextLinePointer;
        currentLineHash = nextLineHash;
        linesProcessed++;
      }
    }

    /**
     * @param line
     * @return
     */
    private Alert createAlertFromLine(String line) {
      Matcher matcher = pattern.matcher(line);
      if(!matcher.matches()) {
        return null;
      }
      Alert a = new Alert();
      // Request
      a.setItem(matcher.group(5));
      a.setItemType("URL");
      a.setType("HTTP Request");
      a.setItemManager("kyriaki.net");
      a.setItemManagerType("Website");
      a.setSeverity(SeverityFactory.getInstance().getSeverity(0));
      a.setLongDescription("Request from " + matcher.group(1) + " for " + matcher.group(5));

      return a;
    }

    private RandomAccessFile openFile() throws FileNotFoundException {
      RandomAccessFile file = new RandomAccessFile(filename, "r");

      return file;
    }

    private boolean isFileLocationValid() throws IOException {
      if (currentLinePointer == -1) {
        return true;
      }

      if (file.length() < currentLinePointer) {
        return false;
      }

      file.seek(currentLinePointer);

      String line = file.readLine();
      if(line == null) {
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

}
