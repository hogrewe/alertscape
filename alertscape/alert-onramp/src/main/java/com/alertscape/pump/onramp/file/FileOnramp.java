/**
 * 
 */
package com.alertscape.pump.onramp.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.pump.onramp.AlertOnramp;

/**
 * @author josh
 * 
 */
public abstract class FileOnramp extends AlertOnramp {
  private static final ASLogger LOG = ASLogger.getLogger(FileOnramp.class);
  private long sleepTimeBetweenReads = 10;
  private String filename;
  private long currentLinePointer;
  private String currentLineHash;
  private FileReadingRunner runner;
  private String regex;
  private Pattern pattern;
  private String asHome;

  public void init() {
    readState();
    runner = new FileReadingRunner();

    Thread t = new Thread(runner);
    t.setName("FileReader::" + getSourceName());
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
        notifyAll();
      }
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
        } catch (Exception e) {
          LOG.error("Problem reading line", e);
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
          if (linesProcessed > 0) {
            long endTime = System.currentTimeMillis();
            long elapsed = endTime - startTime;
            if (elapsed < 1) {
              elapsed = 1;
            }
            long perSecond = (linesProcessed * 1000 / elapsed);
            LOG.info(getSourceName() + ", nothing left to process, done with " + linesProcessed + " lines in "
                + (endTime - startTime) + "ms at " + perSecond + "/s");
            writeState();
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

  protected void writeState() {
    File dir = new File(getAsHome());
    String filename = "." + getSourceName() + ".state";
    File f = new File(dir, filename);
    f.delete();
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    ObjectOutputStream oos = null;
    try {
      fos = new FileOutputStream(f);
      bos = new BufferedOutputStream(fos);
      oos = new ObjectOutputStream(bos);
      oos.writeObject(currentLineHash);
      oos.writeLong(currentLinePointer);
    } catch (IOException e) {
      LOG.error("Couldn't write state to file: " + filename, e);
    } finally {
      if (oos != null) {
        try {
          oos.close();
        } catch (IOException e) {
          LOG.error("Couldn't close stream", e);
        }
      }
    }
  }

  protected void readState() {
    File dir = new File(getAsHome());
    String filename = "." + getSourceName() + ".state";
    File f = new File(dir, filename);
    if (!f.exists()) {
      LOG.info("No state found for " + getSourceName());
      return;
    }
    try {
      FileInputStream fis = new FileInputStream(f);
      BufferedInputStream bis = new BufferedInputStream(fis);
      ObjectInputStream ois = new ObjectInputStream(bis);
      currentLineHash = (String) ois.readObject();
      currentLinePointer = ois.readLong();
      fis.close();
    } catch (Exception e) {
      LOG.error("Couldn't read state from file: " + filename, e);
    }
  }

  /**
   * @param line
   * @return
   */
  protected abstract Alert createAlert(Matcher matcher);

  /**
   * @return the asHome
   */
  public String getAsHome() {
    return asHome;
  }

  /**
   * @param asHome
   *          the asHome to set
   */
  public void setAsHome(String asHome) {
    this.asHome = asHome;
  }

}
