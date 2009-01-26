/**
 * 
 */
package com.alertscape.pump.onramp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.alertscape.common.logging.ASLogger;

/**
 * @author josh
 * 
 */
public abstract class AbstractPollingAlertOnramp extends AlertOnramp {
  private static final ASLogger LOG = ASLogger.getLogger(AbstractPollingAlertOnramp.class);
  private String asHome;
  protected long sleepTimeBetweenReads = 10;
  private PollingRunner runner;

  public void init() {
    Object[] state = readState();
    setState(state);
    runner = new PollingRunner();

    Thread t = new Thread(runner);
    t.setName("PollingOnramp::" + getSourceName());
    t.start();
  }

  protected abstract void setState(Object[] state);

  protected void writeState(Object... state) {
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

      oos.writeObject(state);
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

  protected Object[] readState() {
    File dir = new File(getAsHome());
    String filename = "." + getSourceName() + ".state";
    File f = new File(dir, filename);
    if (!f.exists()) {
      LOG.info("No state found for " + getSourceName());
      return null;
    }
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(f);
      BufferedInputStream bis = new BufferedInputStream(fis);
      ObjectInputStream ois = new ObjectInputStream(bis);
      return (Object[]) ois.readObject();
    } catch (Exception e) {
      LOG.error("Couldn't read state from file: " + filename, e);
      return null;
    } finally {
      if (fis != null) {
        try {
          fis.close();
        } catch (IOException e) {
        }
      }
    }
  }

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

  private class PollingRunner implements Runnable {
    private boolean stopped;

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
      while (!stopped) {
        try {
          readUntilEnd();
        } catch (Exception e) {
          LOG.error("Problem reading alerts", e);
        }
        try {
          synchronized (this) {
            wait(sleepTimeBetweenReads);
          }
        } catch (InterruptedException e) {
        }
      }
    }
  }

  protected abstract void readUntilEnd() throws Exception;

  public void shutdown() {
    runner.setStopped(true);
  }

}
