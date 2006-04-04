/*
 * Created on Apr 1, 2006
 */
package com.alertscape.cev.ui.swing.panel.table.renderer;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.alertscape.cev.model.Event;
import com.alertscape.cev.model.severity.Severity;
import com.alertscape.cev.ui.swing.panel.table.EventCollectionTableModel;

/**
 * @author josh
 * @version $Version: $
 */
public class DefaultEventCellRenderer extends DefaultTableCellRenderer
{
    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component c = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        EventCollectionTableModel model = (EventCollectionTableModel) table
                .getModel( );
        int modelRow = table.convertRowIndexToModel(row);
        Event e = model.getEventAt(modelRow);
        Severity sev = e.getSeverity( );
        if (isSelected)
        {
            // TODO: show the selected colors
        } else
        {
            c.setForeground(sev.getForegroundColor( ));
            c.setBackground(sev.getBackgroundColor( ));
        }

        return c;
    }
}
