/*
 * Created on Aug 16, 2006
 */
package com.alertscape.common.logging;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * @author josh
 * @version $Version: $
 */
public class ASLogger
{
  private static Logger logger = Logger.getLogger(ASLogger.class);
  
  private ASLogger()
  {
    
  }

  /**
   * @param message
   * @param t
   * @see org.apache.log4j.Category#debug(java.lang.Object, java.lang.Throwable)
   */
  public static void debug(Object message, Throwable t)
  {
    logger.debug(message, t);
  }

  /**
   * @param message
   * @see org.apache.log4j.Category#debug(java.lang.Object)
   */
  public static void debug(Object message)
  {
    logger.debug(message);
  }

  /**
   * @param message
   * @param t
   * @see org.apache.log4j.Category#error(java.lang.Object, java.lang.Throwable)
   */
  public static void error(Object message, Throwable t)
  {
    logger.error(message, t);
  }

  /**
   * @param message
   * @see org.apache.log4j.Category#error(java.lang.Object)
   */
  public static void error(Object message)
  {
    logger.error(message);
  }

  /**
   * @param message
   * @param t
   * @see org.apache.log4j.Category#fatal(java.lang.Object, java.lang.Throwable)
   */
  public static void fatal(Object message, Throwable t)
  {
    logger.fatal(message, t);
  }

  /**
   * @param message
   * @see org.apache.log4j.Category#fatal(java.lang.Object)
   */
  public static void fatal(Object message)
  {
    logger.fatal(message);
  }

  /**
   * @param message
   * @param t
   * @see org.apache.log4j.Category#info(java.lang.Object, java.lang.Throwable)
   */
  public static void info(Object message, Throwable t)
  {
    logger.info(message, t);
  }

  /**
   * @param message
   * @see org.apache.log4j.Category#info(java.lang.Object)
   */
  public static void info(Object message)
  {
    logger.info(message);
  }

  /**
   * @return
   * @see org.apache.log4j.Category#isDebugEnabled()
   */
  public static boolean isDebugEnabled( )
  {
    return logger.isDebugEnabled( );
  }

  /**
   * @param level
   * @return
   * @see org.apache.log4j.Category#isEnabledFor(org.apache.log4j.Priority)
   */
  public static boolean isEnabledFor(Priority level)
  {
    return logger.isEnabledFor(level);
  }

  /**
   * @return
   * @see org.apache.log4j.Category#isInfoEnabled()
   */
  public static boolean isInfoEnabled( )
  {
    return logger.isInfoEnabled( );
  }

  /**
   * @return
   * @see org.apache.log4j.Logger#isTraceEnabled()
   */
  public static boolean isTraceEnabled( )
  {
    return logger.isTraceEnabled( );
  }

  /**
   * @param priority
   * @param message
   * @param t
   * @see org.apache.log4j.Category#log(org.apache.log4j.Priority, java.lang.Object, java.lang.Throwable)
   */
  public static void log(Priority priority, Object message, Throwable t)
  {
    logger.log(priority, message, t);
  }

  /**
   * @param priority
   * @param message
   * @see org.apache.log4j.Category#log(org.apache.log4j.Priority, java.lang.Object)
   */
  public static void log(Priority priority, Object message)
  {
    logger.log(priority, message);
  }

  /**
   * @param callerFQCN
   * @param level
   * @param message
   * @param t
   * @see org.apache.log4j.Category#log(java.lang.String, org.apache.log4j.Priority, java.lang.Object, java.lang.Throwable)
   */
  public static void log(String callerFQCN, Priority level, Object message, Throwable t)
  {
    logger.log(callerFQCN, level, message, t);
  }

  /**
   * @param message
   * @param t
   * @see org.apache.log4j.Logger#trace(java.lang.Object, java.lang.Throwable)
   */
  public static void trace(Object message, Throwable t)
  {
    logger.trace(message, t);
  }

  /**
   * @param message
   * @see org.apache.log4j.Logger#trace(java.lang.Object)
   */
  public static void trace(Object message)
  {
    logger.trace(message);
  }

  /**
   * @param message
   * @param t
   * @see org.apache.log4j.Category#warn(java.lang.Object, java.lang.Throwable)
   */
  public static void warn(Object message, Throwable t)
  {
    logger.warn(message, t);
  }

  /**
   * @param message
   * @see org.apache.log4j.Category#warn(java.lang.Object)
   */
  public static void warn(Object message)
  {
    logger.warn(message);
  }

}
