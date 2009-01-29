/**
 * 
 */
package com.alertscape.pump.onramp;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertSource;

/**
 * @author josh
 * 
 */
public class AlertCache implements AlertCacheMBean {
  private static final ASLogger LOG = ASLogger.getLogger(AlertCache.class);
  private int maxSize = 20000;
  private Map<AlertDedupWrapper, CachedAlert> cache = new HashMap<AlertDedupWrapper, CachedAlert>();
  private long lookups;
  private long hits;
  private AlertSource source;

  public AlertCache(AlertSource source) {
    this.source = source;
    registerMBean();
  }

  public synchronized Alert get(AlertDedupWrapper wrapper) {
    lookups++;
    CachedAlert cachedAlert = cache.get(wrapper);
    if (cachedAlert != null) {
      hits++;
      cachedAlert.touch();
      return cachedAlert.getAlertWrapper().getAlert();
    } else {
      return null;
    }
  }

  public synchronized void put(AlertDedupWrapper wrapper) {
    if (cache.size() > maxSize) {
      // Remove 5% so we have room to grow back up
      Set<CachedAlert> lruOrder = new TreeSet<CachedAlert>(cache.values());
      int i = 0;
      int toRemove = (int) (maxSize * 0.05);
      if (toRemove < 1) {
        toRemove = 1;
      }
      Iterator<CachedAlert> lruIter = lruOrder.iterator();
      while (lruIter.hasNext() && i < toRemove) {
        CachedAlert cachedAlert = lruIter.next();
        cache.remove(cachedAlert.getAlertWrapper());
        i++;
      }
    }

    cache.put(wrapper, new CachedAlert(wrapper));
  }

  public synchronized Alert remove(AlertDedupWrapper wrapper) {
    CachedAlert cachedAlert = cache.remove(wrapper);
    return cachedAlert == null ? null : cachedAlert.getAlertWrapper().getAlert();
  }

  public synchronized void clear() {
    cache.clear();
    hits = 0;
    lookups = 0;
  }

  private static class CachedAlert implements Comparable<CachedAlert> {
    private long lastAccessed = System.currentTimeMillis();
    private AlertDedupWrapper alertWrapper;

    /**
     * @param a
     */
    public CachedAlert(AlertDedupWrapper a) {
      this.alertWrapper = a;
    }

    public long getLastAccessed() {
      return lastAccessed;
    }

    public void touch() {
      lastAccessed = System.currentTimeMillis();
    }

    public AlertDedupWrapper getAlertWrapper() {
      return alertWrapper;
    }

    public int compareTo(CachedAlert o) {
      if (lastAccessed < o.lastAccessed) {
        return -1;
      } else if (lastAccessed > o.lastAccessed) {
        return 1;
      } else {
        return 0;
      }
    }
  }

  public float getHitRatio() {
    return (float)hits / (float)lookups;
  }

  public int getSize() {
    return cache.size();
  }

  protected void registerMBean() {
    try {
      MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
      ObjectName name = new ObjectName("com.alertscape:type=onramp,name=onramp-cache-" + source.getSourceName());
      beanServer.registerMBean(this, name);
    } catch (Exception e) {
      LOG.error("Couldn't register mbean for onramp: " + source.getSourceName(), e);
    }
  }

}
