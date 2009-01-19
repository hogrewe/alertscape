/**
 * 
 */
package com.alertscape.browser.ui.swing.panel.collection.table;

import java.util.Collections;
import java.util.List;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.TableFormat;

import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertAttributeDefinition;

/**
 * @author josh
 * 
 */
public class AlertTableFormat implements TableFormat<Alert> {
  private TableFormat<Alert> delegate;
  private List<AlertAttributeDefinition> definitions;

  public AlertTableFormat(List<String> fieldNames, List<String> fieldLabels, List<AlertAttributeDefinition> definitions) {
    String[] fieldNameArray = new String[fieldNames.size()];
    fieldNameArray = fieldNames.toArray(fieldNameArray);
    String[] fieldLabelArray = new String[fieldLabels.size()];
    fieldLabelArray = fieldLabels.toArray(fieldLabelArray);
    delegate = GlazedLists.tableFormat(Alert.class, fieldNameArray, fieldLabelArray);

    this.definitions = definitions;

    if (this.definitions == null) {
      this.definitions = Collections.emptyList();
    }
  }

  /**
   * @return
   * @see ca.odell.glazedlists.gui.TableFormat#getColumnCount()
   */
  public int getColumnCount() {
    return delegate.getColumnCount() + definitions.size();
  }

  /**
   * @param arg0
   * @return
   * @see ca.odell.glazedlists.gui.TableFormat#getColumnName(int)
   */
  public String getColumnName(int col) {
    if (col < delegate.getColumnCount()) {
      return delegate.getColumnName(col);
    } else {
      AlertAttributeDefinition def = definitions.get(col - delegate.getColumnCount());
      String label = def.getDisplayName();
      return label == null ? def.getName() : label;
    }
  }

  /**
   * @param arg0
   * @param arg1
   * @return
   * @see ca.odell.glazedlists.gui.TableFormat#getColumnValue(java.lang.Object, int)
   */
  public Object getColumnValue(Alert a, int col) {
    if (col < delegate.getColumnCount()) {
      return delegate.getColumnValue(a, col);
    } else {
      AlertAttributeDefinition def = definitions.get(col - delegate.getColumnCount());
      return a.getExtendedAttribute(def.getName());
    }
  }
}
