/**
 * 
 */
package com.alertscape.pump.onramp;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.alertscape.AlertscapeException;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.equator.AlertEquator;
import com.alertscape.common.model.severity.Severity;
import com.alertscape.common.model.severity.SeverityFactory;

/**
 * @author josh
 * 
 */
public abstract class AbstractPollingAlertOnramp extends AlertOnramp implements AbstractPollingAlertOnrampMBean {
  private static final ASLogger LOG = ASLogger.getLogger(AbstractPollingAlertOnramp.class);
  protected long sleepTimeBetweenReads = 10;
  private boolean stopped;
  private PollingRunner runner;
  
  private List<String> uniqueFields;
  private String severityDeterminedField;
  private Map<Integer, List<String>> severityMappings;
  private Method severityFieldGetter;


  public void init() throws AlertscapeException {
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
            wait(sleepTimeBetweenReads * slowdown);
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
    if (uniqueFields == null) {
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
