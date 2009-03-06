/**
 * 
 */
package com.alertscape.browser.model.tree;

import ca.odell.glazedlists.matchers.Matcher;
import ca.odell.glazedlists.matchers.Matchers;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class FieldAlertMatcher implements AlertMatcher {
  private Matcher<Alert> delegate;
  private String fieldName;
  private Object value;

  public FieldAlertMatcher(String fieldName, Object value) {
    this.fieldName = fieldName;
    this.value = value;
    createDelegate();
  }

  public FieldAlertMatcher() {

  }

  @Override
  public boolean matches(Alert a) {
    return delegate.matches(a);
  }

  /**
   * 
   */
  private void createDelegate() {
    if (fieldName != null && value != null) {
      delegate = Matchers.beanPropertyMatcher(Alert.class, fieldName, value);
    }
  }

  /**
   * @return the fieldName
   */
  public String getFieldName() {
    return fieldName;
  }

  /**
   * @param fieldName
   *          the fieldName to set
   */
  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
    createDelegate();
  }

  /**
   * @return the value
   */
  public Object getValue() {
    return value;
  }

  /**
   * @param value
   *          the value to set
   */
  public void setValue(Object value) {
    this.value = value;
    createDelegate();
  }
}
