/**
 * 
 */
package com.alertscape.pump.onramp.equator;

import com.alertscape.common.model.Alert;

abstract class AbstractAlertPropertyEquator implements AlertPropertyEquator {
  private boolean ignoreCase;

  public final boolean equal(Alert a1, Alert a2) {
    Object value1 = getValue(a1);
    Object value2 = getValue(a2);

    if (ignoreCase && value1 instanceof String && value2 instanceof String) {
      String s1 = (String) value1;
      String s2 = (String) value2;
      return s1.equalsIgnoreCase(s2);
    }

    if (value1 != null) {
      return value1.equals(value2);
    } else {
      return value2 == null;
    }
  }

  public Object getHashableValue(Alert a) {
    Object val = getValue(a);
    if (val != null && ignoreCase && val instanceof String) {
      return ((String) val).toUpperCase();
    }

    return val;
  }

  protected abstract Object getValue(Alert a);
}