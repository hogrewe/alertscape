/*
 * Created on Apr 1, 2006
 */
package com.alertscape.cev.ui.swing.panel.collection.table.renderer;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import ca.odell.glazedlists.swing.EventTableModel;

import com.alertscape.cev.model.Event;
import com.alertscape.cev.model.severity.Severity;

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

        EventTableModel<Event> model = (EventTableModel<Event>) table.getModel( );
        Event e = model.getElementAt(row);
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
