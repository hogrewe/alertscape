/**
 * 
 */
package com.alertscape.pump.onramp.file;

import java.beans.BeanDescriptor;
import java.beans.Beans;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class SimpleRegexLineProcessor implements AlertLineProcessor {
  private static final ASLogger LOG = ASLogger.getLogger(SimpleRegexLineProcessor.class);
  private String regex;
  private Pattern pattern;
  private boolean matchRequired = true;
  private Map<String, Method> cachedSetters = new HashMap<String, Method>();
  private Map<String, String> fieldMappings;
  private Map<String, String> categoryMappings;

  public Alert createAlert(String line) {
    Alert a = new Alert();

    populateAlert(a, line);

    return a;
  }

  public Alert populateAlert(Alert a, String line) {
    Matcher matcher = pattern.matcher(line);

    if (!matcher.matches()) {
      if (isMatchRequired()) {
        return null;
      } else {
        return a;
      }
    }

    if (getFieldMappings() != null) {
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
    }

    Map<String, Object> extendedAttributes = a.getExtendedAttributes();
    if (getCategoryMappings() != null) {
      for (String field : getCategoryMappings().keySet()) {
        // TODO: Is this a good idea?
        String replacement = getCategoryMappings().get(field);
        String value = matcher.replaceAll(replacement);
        matcher.reset();
        extendedAttributes.put(field, value);
      }
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
      String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      setter = Alert.class.getMethod(methodName, String.class);
      cachedSetters.put(fieldName, setter);
    } catch (Exception e) {
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

  /**
   * @return the regex
   */
  public String getRegex() {
    return regex;
  }

  /**
   * @param regex
   *          the regex to set
   */
  public void setRegex(String regex) {
    if (regex == null) {
      this.regex = null;
      pattern = null;
    } else {
      this.regex = regex.trim();
      pattern = Pattern.compile(this.regex);
    }
  }

  /**
   * @param matchRequired
   *          the matchRequired to set
   */
  public void setMatchRequired(boolean matchRequired) {
    this.matchRequired = matchRequired;
  }

  /**
   * @return the matchRequired
   */
  public boolean isMatchRequired() {
    return matchRequired;
  }

}
