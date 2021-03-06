/**
 * 
 */
package com.alertscape.pump.onramp;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alertscape.AlertscapeException;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.AlertStatus;
import com.alertscape.common.model.equator.AlertEquator;
import com.alertscape.pump.AlertSourceCallback;
import com.alertscape.pump.onramp.line.AlertLineProcessor;
import com.alertscape.pump.onramp.sender.AlertTransport;
import com.alertscape.pump.onramp.sender.AlertTransportException;

/**
 * @author josh
 * 
 */
public abstract class AlertOnramp implements AlertSourceCallback {
  private static ASLogger LOG = ASLogger.getLogger(AlertOnramp.class);
  protected static final String ALERT_ID_STATE = "alertId";
  private AlertTransport transport;
  private long nextAlertId;
  private AlertEquator equator;
  private AlertCache cache;
  private AlertSource source = new AlertSource(1, "UNKNOWN");
  private String sourceName;
  private long alertsProcessed;
  private String asHome;
  private String stateFilename;
  protected Map<String, Object> state = new HashMap<String, Object>();
  private File stateFile;
  private String postProcessField;
  private Method postProcessGetter;
  private List<AlertLineProcessor> postProcessors;
  private List<MultiClearCriteria> clearCriteria;

  public void sendAlert(Alert alert) {
    alert.setSource(source);

    if (clearCriteria != null) {
      for (MultiClearCriteria clearCrit : clearCriteria) {
        if (clearCrit.shouldClear(alert)) {
          List<Alert> matchingAlerts = transport.findMatchingAlerts(alert, clearCrit.getToClearEquator());
          LOG.info("Clearing out " + matchingAlerts.size() + " alerts from : " + alert);
          for (Alert matched : matchingAlerts) {
            matched.setStatus(AlertStatus.CLEARED);
            updateCached(matched);
            try {
              transport.sendAlert(matched);
            } catch (AlertTransportException e) {
              LOG.error("Couldn't send alert to transport", e);
            }
          }
          // We cleared some alerts, and now we're done
          return;
        }
      }
    }

    if (postProcessGetter != null && postProcessors != null) {
      try {
        Object value = postProcessGetter.invoke(alert, (Object[]) null);
        if (value != null) {
          for (AlertLineProcessor processor : postProcessors) {
            processor.populateAlert(alert, value.toString());
          }
        }
      } catch (Exception e) {
        LOG.error("Couldn't get " + postProcessField + " from alert", e);
      }
    }

    Date now = new Date();

    Alert existing = updateCached(alert);

    if (existing != null) {
      alert.setAlertId(existing.getAlertId());
      alert.setCount(existing.getCount() + 1);
      alert.setFirstOccurence(existing.getFirstOccurence());
      
      // Set the non-onramp (i.e. user defined) attributes
      alert.setMajorTags(existing.getMajorTags());
      alert.setMinorTags(existing.getMinorTags());
    } else {
      alert.setAlertId(nextAlertId++);
      alert.setCount(1);
      if (alert.getFirstOccurence() == null) {
        alert.setFirstOccurence(now);
      }
      state.put(ALERT_ID_STATE, nextAlertId);
    }

    if (alert.getLastOccurence() == null) {
      alert.setLastOccurence(now);
    }

    try {
      transport.sendAlert(alert);
      alertsProcessed++;
    } catch (AlertTransportException e) {
      LOG.error("Couldn't send alert to transport", e);
    }
  }

  public Alert updateCached(Alert alert) {
    AlertDedupWrapper alertWrapper = new AlertDedupWrapper(equator, alert);
    Alert existing;
    if (!alert.isStanding()) {
      return cache.remove(alertWrapper);
    }

    existing = cache.get(alertWrapper);
    if (existing == null) {
      existing = transport.getAlert(alert, equator);
    }
    cache.put(alertWrapper);
    return existing;
  }

  public final void onrampInit() throws AlertscapeException {
    try {
      if (source == null) {
        source = transport.getSource(sourceName);
      } else {
        sourceName = source.getSourceName();
      }
      nextAlertId = transport.registerAlertSource(source, this);
      stateFilename = "." + getSourceName() + ".state";
      File dir = new File(getAsHome());
      stateFile = new File(dir, stateFilename);

      cache = new AlertCache(source);

      state = readState();
      if (state != null) {
        Object o = state.get(ALERT_ID_STATE);
        if (o != null) {
          nextAlertId = (Long) o;
        }
        initState(state);
      }
    } catch (AlertTransportException e) {
      LOG.error("Couldn't get initalize from transport", e);
    }

    init();
  }

  public void updateAlert(Alert alert) {
    updateCached(alert);
  }

  protected abstract void init() throws AlertscapeException;

  public abstract void shutdown();

  public AlertTransport getTransport() {
    return transport;
  }

  public void setTransport(AlertTransport sender) {
    this.transport = sender;
  }

  public AlertEquator getEquator() {
    return equator;
  }

  public void setEquator(AlertEquator equator) {
    this.equator = equator;
  }

  /**
   * @return the source
   */
  public AlertSource getSource() {
    return source;
  }

  /**
   * @param source
   *          the source to set
   */
  public void setSource(AlertSource source) {
    this.source = source;
  }

  /**
   * @return the sourceName
   */
  public String getSourceName() {
    return sourceName;
  }

  /**
   * @param sourceName
   *          the sourceName to set
   */
  public void setSourceName(String sourceName) {
    this.sourceName = sourceName;
  }

  public long getAlertsProcessed() {
    return alertsProcessed;
  }

  public void clearAlertsProcessed() {
    alertsProcessed = 0;
  }

  public void saveState() {
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    ObjectOutputStream stateOutputStream = null;
    try {
      fos = new FileOutputStream(stateFile);
      bos = new BufferedOutputStream(fos);
      stateOutputStream = new ObjectOutputStream(bos);
      stateOutputStream.writeObject(state);
    } catch (IOException e) {
      LOG.error("Couldn't write state to file: " + stateFilename, e);
    } finally {
      if (stateOutputStream != null) {
        try {
          stateOutputStream.close();
        } catch (IOException e) {
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  public Map<String, Object> readState() {
    if (!stateFile.exists()) {
      LOG.info("No state found for " + getSourceName());
      return new HashMap<String, Object>();
    }
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(stateFile);
      BufferedInputStream bis = new BufferedInputStream(fis);
      ObjectInputStream ois = new ObjectInputStream(bis);
      Map<String, Object> state = (Map<String, Object>) ois.readObject();
      if (state == null) {
        state = new HashMap<String, Object>();
      }
      return state;
    } catch (Exception e) {
      LOG.error("Couldn't read state from file: " + stateFilename, e);
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

  protected abstract void initState(Map<String, Object> state);

  /**
   * @return the postProcessField
   */
  public String getPostProcessField() {
    return postProcessField;
  }

  /**
   * @param postProcessField
   *          the postProcessField to set
   */
  public void setPostProcessField(String postProcessField) {
    this.postProcessField = postProcessField;
    if (postProcessField != null) {
      try {
        PropertyDescriptor d = new PropertyDescriptor(postProcessField, Alert.class);
        postProcessGetter = d.getReadMethod();
      } catch (IntrospectionException e) {
        LOG.error("Couldn't create getter for " + postProcessField, e);
        postProcessGetter = null;
      }
    } else {
      postProcessGetter = null;
    }
  }

  /**
   * @return the postProcessor
   */
  public List<AlertLineProcessor> getPostProcessors() {
    return postProcessors;
  }

  /**
   * @param postProcessors
   *          the postProcessor to set
   */
  public void setPostProcessors(List<AlertLineProcessor> postProcessors) {
    this.postProcessors = postProcessors;
  }

  /**
   * @return the clearCriteria
   */
  public List<MultiClearCriteria> getClearCriteria() {
    return clearCriteria;
  }

  /**
   * @param clearCriteria
   *          the clearCriteria to set
   */
  public void setClearCriteria(List<MultiClearCriteria> clearCriteria) {
    this.clearCriteria = clearCriteria;
  }
}
