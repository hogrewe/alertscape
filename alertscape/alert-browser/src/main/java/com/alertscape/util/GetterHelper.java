/*
 * Created on Apr 1, 2006
 */
package com.alertscape.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;

/**
 * @author josh
 * @version $Version: $
 */
public class GetterHelper {
  private static final ASLogger LOG = ASLogger.getLogger(GetterHelper.class);
  private static final Class<Alert> ALERT_CLASS = Alert.class;

  public static Method makeAlertGetter(String fieldName) {
    return makeGetter(ALERT_CLASS, fieldName);
  }

  public static Method makeGetter(Class<?> c, String fieldName) {
    Method getter = null;
    try {
      PropertyDescriptor d = new PropertyDescriptor(fieldName, c);
      getter = d.getReadMethod();
    } catch (IntrospectionException e) {
      LOG.error("Couldn't make getter for " + fieldName + " in class " + c, e);
    }
    return getter;
  }
}
