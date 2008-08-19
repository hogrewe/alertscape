/**
 * 
 */
package com.alertscape.pump.onramp.equator;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.alertscape.common.model.Alert;

final class AttributeEquator extends AbstractAlertPropertyEquator {
  protected static final Object[] NO_ARGS = new Object[0];

  private String attributeName;
  private Method method;

  public AttributeEquator(String attributeName) {
    try {
      this.attributeName = attributeName;
      PropertyDescriptor descriptor = new PropertyDescriptor(attributeName, Alert.class);
      method = descriptor.getReadMethod();
    } catch (IntrospectionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  protected Object getValue(Alert a) {
    try {
      return method.invoke(a, NO_ARGS);
    } catch (IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public String toString() {
    return "Attribute:" + attributeName;
  }

}