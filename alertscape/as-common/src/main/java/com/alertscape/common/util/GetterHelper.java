/*
 * Created on Apr 1, 2006
 */
package com.alertscape.common.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;

/**
 * @author josh
 * @version $Version: $
 */
public class GetterHelper {
  private static final ASLogger LOG = ASLogger.getLogger(GetterHelper.class);
  private static Map<String, Method> cachedGetters = new HashMap<String, Method>();

  public static Method makeGetter(String fieldName) {
    Method getter = cachedGetters.get(fieldName);
    if (getter != null) {
      return getter;
    }

    try {
      String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      getter = Alert.class.getMethod(methodName, new Class<?>[0]);
      cachedGetters.put(fieldName, getter);
    } catch (Exception e) {
      LOG.error("Couldn't make getter for " + fieldName, e);
    }
    return getter;
  }
}
