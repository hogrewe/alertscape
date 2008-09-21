package com.alertscape.browser.ui.swing.panel.collection.table.renderer;

import java.awt.Component;
import java.util.Date;

import javax.swing.JTable;

import com.alertscape.util.FormatHelper;

public class DateAlertCellRenderer extends DefaultAlertCellRenderer
{
    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Date date = (Date) value;
        value = FormatHelper.formatDate(date);

        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                row, column);
    }

    
} 
