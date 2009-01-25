/*
 * Created on Apr 2, 2006
 */
package com.alertscape.browser.ui.swing.panel.collection.table.renderer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.alertscape.common.model.severity.Severity;
import com.alertscape.util.ImageFinder;

/**
 * @author josh
 * @version $Version: $
 */
public class SeverityAlertCellRenderer extends DefaultAlertCellRenderer
{
    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Severity sev = (Severity) value;
        value = sev.getName();

        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        l.setIcon(ImageFinder.getInstance().findImage(sev.getSmallIcon()));
        
        return l;
    }

    
}
