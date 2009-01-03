/*
 * Created on Apr 1, 2006
 */
package com.alertscape.browser.ui.swing.panel.collection.table;

import java.lang.reflect.Method;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.util.GetterHelper;
import com.alertscape.util.PrimitiveHelper;

class AlertColumn {
  private static final ASLogger LOG = ASLogger.getLogger(AlertColumn.class);
  String displayName;
  String propertyName;
  private Method propertyGetter;
  private Class<?> columnClass;
  private int minWidth;
  private int maxWidth;
  private int width;

  public AlertColumn(String displayName, String propertyName) {
    setDisplayName(displayName);
    setPropertyName(propertyName);
  }

  public Class<?> getColumnClass() {
    return columnClass;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getPropertyName() {
    return propertyName;
  }

  public void setPropertyName(String propertyName) {
    if (this.propertyName == null || !this.propertyName.equals(propertyName)) {
      propertyGetter = GetterHelper.makeEventGetter(propertyName);
      if (propertyGetter != null) {
        columnClass = propertyGetter.getReturnType();
        if (columnClass.isPrimitive()) {
          columnClass = PrimitiveHelper.getContainingClass(columnClass);
        }
      }
    }
    this.propertyName = propertyName;
  }

  public int getMaxWidth() {
    return maxWidth;
  }

  public void setMaxWidth(int maxWidth) {
    this.maxWidth = maxWidth;
  }

  public int getMinWidth() {
    return minWidth;
  }

  public void setMinWidth(int minWidth) {
    this.minWidth = minWidth;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public Object getValue(Alert e) {
    Object o = null;
    if (propertyGetter != null) {
      try {
        o = propertyGetter.invoke(e, new Object[0]);
      } catch (Exception e1) {
        LOG.error("Couldn't get value for column " + getDisplayName(), e1);
      }
    }
    return o;
  }

  public Method getPropertyGetter() {
    return propertyGetter;
  }
}