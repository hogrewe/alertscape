/**
 * 
 */
package com.alertscape.pump.offramp;

import java.util.ArrayList;
import java.util.List;

import com.alertscape.AlertscapeException;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
@SuppressWarnings("unchecked")
public class QueuingOfframp implements AlertOfframp {
  private static final ASLogger LOG = ASLogger.getLogger(QueuingOfframp.class);

  private List[] queuedAlerts = { new ArrayList(), new ArrayList() };
  private int currentQueue = 0;
  private AlertProcessor processor = new AlertProcessor();
  private boolean stopped = false;
  private int maxQueueSize = 5000;
  private AlertOfframp queuedOfframp;

  public void init() {
    Thread t = new Thread(processor);
    t.setName(getOfframpName() + "::AlertProcessor");
    t.start();
  }

  public void shutdown() {
    synchronized (this) {
      stopped = true;
      notifyAll();
    }
  }

  public final void processAlert(Alert alert) throws AlertscapeException {
    synchronized (queuedAlerts) {
      while (queuedAlerts[currentQueue].size() > getMaxQueueSize()) {
        try {
          queuedAlerts.wait(1000);
        } catch (InterruptedException e) {
        }
        queuedAlerts[currentQueue].add(alert);
      }
    }
  }
  
  public String getOfframpName() {
    return getQueuedOfframp().getOfframpName();
  }

  protected void processQueuedAlert(Alert alert) throws AlertscapeException {
    getQueuedOfframp().processAlert(alert);
  }

  private class AlertProcessor implements Runnable {
    public void run() {
      while (!stopped) {
        List<Alert> queue = queuedAlerts[currentQueue];
        synchronized (queuedAlerts) {
          currentQueue = 1 - currentQueue;
        }
        processQueue(queue);
        synchronized (this) {
          try {
            wait(1000);
          } catch (InterruptedException e) {
          }
        }
      }
      processQueue(queuedAlerts[currentQueue]);
      processQueue(queuedAlerts[1 - currentQueue]);
    }
  }

  private void processQueue(List<Alert> queue) {
    synchronized (queue) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Processing " + queue.size() + " queued alerts");
      }
      long start = System.currentTimeMillis();
      for (Alert alert : queue) {
        try {
          processQueuedAlert(alert);
        } catch (AlertscapeException e) {
          LOG.error("Couldn't process alert " + alert, e);
        }
      }
      long end = System.currentTimeMillis();
      if (queue.size() > 0) {
        long duration = end - start;
        LOG.info(getOfframpName() + " processed " + queue.size() + " alerts asynchronously in " + duration + " ms at "
            + queue.size() * 1000 / duration + "/s");
      }

      queue.clear();
    }
  }

  /**
   * @return the maxQueueSize
   */
  public int getMaxQueueSize() {
    return maxQueueSize;
  }

  /**
   * @param maxQueueSize
   *          the maxQueueSize to set
   */
  public void setMaxQueueSize(int maxQueueSize) {
    this.maxQueueSize = maxQueueSize;
  }

  /**
   * @return the queuedOfframp
   */
  public AlertOfframp getQueuedOfframp() {
    return queuedOfframp;
  }

  /**
   * @param queuedOfframp
   *          the queuedOfframp to set
   */
  public void setQueuedOfframp(AlertOfframp queuedOfframp) {
    this.queuedOfframp = queuedOfframp;
  }

}
