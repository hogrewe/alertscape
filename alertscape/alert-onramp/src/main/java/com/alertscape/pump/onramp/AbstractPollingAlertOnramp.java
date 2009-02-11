/**
 * 
 */
package com.alertscape.pump.onramp;

import java.lang.management.ManagementFactory;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.alertscape.common.logging.ASLogger;

/**
 * @author josh
 * 
 */
public abstract class AbstractPollingAlertOnramp extends AlertOnramp implements AbstractPollingAlertOnrampMBean {
  private static final ASLogger LOG = ASLogger.getLogger(AbstractPollingAlertOnramp.class);
  protected long sleepTimeBetweenReads = 10;
  private boolean stopped;
  private PollingRunner runner;

  public void init() {
    registerMBean();
    runner = new PollingRunner();
    start();
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
    synchronized (runner) {
      this.stopped = stopped;
      runner.notifyAll();
    }
  }

  private class PollingRunner implements Runnable {
    private static final int MAX_SLOWDOWN_MULTIPLIER = 128;

    public void run() {
      int slowdown = 1;
      while (!stopped) {
        try {
          int processed = readUntilEnd();
          if(processed < 1 && slowdown < MAX_SLOWDOWN_MULTIPLIER) {
            slowdown *= 2;
          } else {
            slowdown = 1;
          }
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

  protected abstract int readUntilEnd() throws Exception;

  public void shutdown() {
    setStopped(true);
  }

  protected void registerMBean() {
    try {
      MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
      ObjectName name = new ObjectName("com.alertscape:type=onramp,name=onramp-" + getSourceName());
      beanServer.registerMBean(this, name);
    } catch (Exception e) {
      LOG.error("Couldn't register mbean for onramp: " + getSourceName(), e);
    }
  }

  public void start() {
    stopped = false;
    Thread t = new Thread(runner);
    t.setName("PollingOnramp::" + getSourceName());
    t.start();
  }

  public void stop() {
    setStopped(true);
  }

  public long showAlertsProcessed() {
    return getAlertsProcessed();
  }

  public Map<String, Object> showState() {
    return state;
  }

}
