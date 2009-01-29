/**
 * 
 */
package com.alertscape.common.model.equator;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.alertscape.AlertscapeException;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;

public final class AttributeEquator extends AbstractAlertPropertyEquator {
  private static final long serialVersionUID = -2121846549972224268L;
  private static final ASLogger LOG = ASLogger.getLogger(AttributeEquator.class);
  protected static final Object[] NO_ARGS = new Object[0];

  private String attributeName;
  private Method method;

  public AttributeEquator(String attributeName) throws AlertscapeException {
    try {
      this.attributeName = attributeName;
      PropertyDescriptor descriptor = new PropertyDescriptor(attributeName, Alert.class);
      method = descriptor.getReadMethod();
    } catch (IntrospectionException e) {
      throw new AlertscapeException("Couldn't create equator for attribute: " + attributeName, e);
    }
  }

  public String getAttributeName() {
    return attributeName;
  }

  @Override
  public Object getValue(Alert a) {
    try {
      return method.invoke(a, NO_ARGS);
    } catch (IllegalArgumentException e) {
      LOG.error(e);
    } catch (IllegalAccessException e) {
      LOG.error(e);
    } catch (InvocationTargetException e) {
      LOG.error(e);
    }
    return null;
  }

  @Override
  public String toString() {
    return "Attribute:" + attributeName;
  }

}