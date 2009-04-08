/**
 * 
 */
package com.alertscape.pump.onramp;

import java.lang.reflect.Method;
import java.util.List;

import com.alertscape.AlertscapeException;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.equator.AlertEquator;
import com.alertscape.common.util.GetterHelper;

/**
 * @author josh
 * 
 */
public class MultiClearCriteria {
  private static final ASLogger LOG = ASLogger.getLogger(MultiClearCriteria.class);
  private String clearCriteriaField;
  private Method clearGetter;
  private String clearCriteriaValue;
  private AlertEquator toClearEquator;
  private List<String> toClearFields;

  public boolean shouldClear(Alert a) {
    if (clearGetter != null) {
      try {
        Object object;
        object = clearGetter.invoke(a, (Object[]) null);
        if (object == null && clearCriteriaValue == null) {
          return true;
        } else if (object != null) {
          return object.toString().equalsIgnoreCase(clearCriteriaValue);
        }
      } catch (Exception e) {
        LOG.error("Couldn't invoke getter for: " + clearGetter + " on " + a, e);
      }
    }
    return false;
  }

  public void setToClearFields(List<String> fields) throws AlertscapeException {
    toClearEquator = new AlertEquator(fields);
    this.toClearFields = fields;
  }

  public List<String> getToClearFields() {
    return toClearFields;
  }

  /**
   * @return the clearEquator
   */
  public AlertEquator getToClearEquator() {
    return toClearEquator;
  }

  /**
   * @param clearEquator
   *          the clearEquator to set
   */
  public void setToClearEquator(AlertEquator clearEquator) {
    this.toClearEquator = clearEquator;
  }

  /**
   * @return the clearField
   */
  public String getClearCriteriaField() {
    return clearCriteriaField;
  }

  /**
   * @param clearField
   *          the clearField to set
   */
  public void setClearCriteriaField(String clearField) {
    this.clearCriteriaField = clearField;
    if (clearField == null) {
      clearGetter = null;
    } else {
      clearGetter = GetterHelper.makeGetter(clearField);
    }
  }

  /**
   * @return the clearValue
   */
  public Object getClearCriteriaValue() {
    return clearCriteriaValue;
  }

  /**
   * @param clearValue
   *          the clearValue to set
   */
  public void setClearCriteriaValue(String clearValue) {
    this.clearCriteriaValue = clearValue;
  }
}
