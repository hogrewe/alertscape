/*
 * Created on Aug 16, 2006
 */
package com.alertscape.common.logging;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * @author josh
 * @version $Version: $
 */
public class ASLogger {
  private static Map<String, ASLogger> loggerMap = new HashMap<String, ASLogger>();
  private Logger logger;

  private ASLogger(Logger logger) {
    this.logger = logger;
  }

  public static ASLogger getLogger(String name) {
    synchronized (loggerMap) {
      ASLogger aslog = loggerMap.get(name);
      if (aslog == null) {
        aslog = new ASLogger(Logger.getLogger(name));
        loggerMap.put(name, aslog);
      }

      return aslog;
    }
  }

  public static ASLogger getLogger(Class<?> clazz) {
    return getLogger(clazz == null ? "<null>" : clazz.getName());
  }

  public void debug(Object message, Throwable t) {
    logger.debug(message, t);
  }

  public void debug(Object message) {
    logger.debug(message);
  }

  public void error(Object message, Throwable t) {
    logger.error(message, t);
  }

  public void error(Object message) {
    logger.error(message);
  }

  public void fatal(Object message, Throwable t) {
    logger.fatal(message, t);
  }

  public void fatal(Object message) {
    logger.fatal(message);
  }

  public void info(Object message, Throwable t) {
    logger.info(message, t);
  }

  public void info(Object message) {
    logger.info(message);
  }

  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  public boolean isEnabledFor(Priority level) {
    return logger.isEnabledFor(level);
  }

  public boolean isInfoEnabled() {
    return logger.isInfoEnabled();
  }

  public boolean isTraceEnabled() {
    return logger.isTraceEnabled();
  }

  public void log(Priority priority, Object message, Throwable t) {
    logger.log(priority, message, t);
  }

  public void log(Priority priority, Object message) {
    logger.log(priority, message);
  }

  public void log(String callerFQCN, Priority level, Object message, Throwable t) {
    logger.log(callerFQCN, level, message, t);
  }

  public void trace(Object message, Throwable t) {
    logger.trace(message, t);
  }

  public void trace(Object message) {
    logger.trace(message);
  }

  public void warn(Object message, Throwable t) {
    logger.warn(message, t);
  }

  public void warn(Object message) {
    logger.warn(message);
  }

}
