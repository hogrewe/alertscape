/*
 * Created on Apr 1, 2006
 */
package com.alertscape.browser.ui.swing.panel.collection.table.renderer;

import java.awt.Component;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import ca.odell.glazedlists.swing.EventTableModel;

import com.alertscape.common.model.Alert;
import com.alertscape.common.model.severity.Severity;
import com.alertscape.util.FormatHelper;

/**
 * @author josh
 * @version $Version: $
 */
public class DefaultAlertCellRenderer extends DefaultTableCellRenderer {
  private static final long serialVersionUID = 1L;

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
      int row, int column) {
    if(value instanceof Date) {
      Date date = (Date) value;
      value = FormatHelper.formatDate(date);
    }
    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

    @SuppressWarnings("unchecked")
    EventTableModel<Alert> model = (EventTableModel<Alert>) table.getModel();
    Alert e = model.getElementAt(row);
    Severity sev = e.getSeverity();
    if (isSelected) {
      // TODO: show the selected colors
      // c.setForeground(sev.getSelectionForegroundColor());
      // c.setBackground(sev.getSelectionBackgroundColor());
    } else {
      c.setForeground(sev.getForegroundColor());
      c.setBackground(sev.getBackgroundColor());
    }

    return c;
  }
}
