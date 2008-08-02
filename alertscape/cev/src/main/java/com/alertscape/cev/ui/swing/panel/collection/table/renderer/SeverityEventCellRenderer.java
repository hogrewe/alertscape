/*
 * Created on Apr 2, 2006
 */
package com.alertscape.cev.ui.swing.panel.collection.table.renderer;

import java.awt.Component;

import javax.swing.JTable;

import com.alertscape.common.model.severity.Severity;

/**
 * @author josh
 * @version $Version: $
 */
public class SeverityEventCellRenderer extends DefaultEventCellRenderer
{
    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Severity sev = (Severity) value;
        value = sev.getName();

        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                row, column);
    }

    
}
