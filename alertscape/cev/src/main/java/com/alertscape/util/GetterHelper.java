/*
 * Created on Apr 1, 2006
 */
package com.alertscape.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import com.alertscape.cev.model.Event;
import com.alertscape.common.logging.ASLogger;

/**
 * @author josh
 * @version $Version: $
 */
public class GetterHelper
{
  private static final Class<Event> EVENT_CLASS = Event.class;

  public static Method makeEventGetter(String fieldName)
  {
    return makeGetter(EVENT_CLASS, fieldName);
  }

  public static Method makeGetter(Class<?> c, String fieldName)
  {
    Method getter = null;
    try
    {
      PropertyDescriptor d = new PropertyDescriptor(fieldName, c);
      getter = d.getReadMethod( );
    }
    catch (IntrospectionException e)
    {
      ASLogger.error("Couldn't make getter for " + fieldName + " in class " + c,
          e);
    }
    return getter;
  }
}
