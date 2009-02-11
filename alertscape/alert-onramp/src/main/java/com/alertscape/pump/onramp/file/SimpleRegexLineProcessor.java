/**
 * 
 */
package com.alertscape.pump.onramp.file;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class SimpleRegexLineProcessor implements AlertLineProcessor {
  private static final ASLogger LOG = ASLogger.getLogger(SimpleRegexLineProcessor.class);
  private Map<String, Method> cachedSetters = new HashMap<String, Method>();
  private Map<String, String> fieldMappings;
  private Map<String, String> categoryMappings;

  public Alert createAlert(Matcher matcher) {
    Alert a = new Alert();

    for (String field : getFieldMappings().keySet()) {
      // TODO: Is this a good idea?
      String replacement = getFieldMappings().get(field);
      String value = matcher.replaceAll(replacement);
      matcher.reset();
      Method setter = getSetter(field);
      try {
        setter.invoke(a, new Object[] { value });
      } catch (Exception e) {
        LOG.error("Couldn't set field " + field + " on alert with value " + value, e);
      }
    }

    Map<String, Object> extendedAttributes = new HashMap<String, Object>();
    for (String field : getCategoryMappings().keySet()) {
      // TODO: Is this a good idea?
      String replacement = getCategoryMappings().get(field);
      String value = matcher.replaceAll(replacement);
      matcher.reset();
      extendedAttributes.put(field, value);
    }
    a.setExtendedAttributes(extendedAttributes);

    return a;
  }

  protected Method getSetter(String fieldName) {
    Method setter = cachedSetters.get(fieldName);
    if (setter != null) {
      return setter;
    }

    try {
      PropertyDescriptor d = new PropertyDescriptor(fieldName, Alert.class);
      setter = d.getWriteMethod();
      cachedSetters.put(fieldName, setter);
    } catch (IntrospectionException e) {
      LOG.error("Couldn't make setter for " + fieldName, e);
    }
    return setter;
  }

  /**
   * @return the fieldMappings
   */
  public Map<String, String> getFieldMappings() {
    return fieldMappings;
  }

  /**
   * @param fieldMappings
   *          the fieldMappings to set
   */
  public void setFieldMappings(Map<String, String> fieldMappings) {
    this.fieldMappings = fieldMappings;
  }

  /**
   * @return the categoryMappings
   */
  public Map<String, String> getCategoryMappings() {
    return categoryMappings;
  }

  /**
   * @param categoryMappings
   *          the categoryMappings to set
   */
  public void setCategoryMappings(Map<String, String> categoryMappings) {
    this.categoryMappings = categoryMappings;
  }

}
