/*
 * Created on Apr 1, 2006
 */
package com.alertscape.browser.ui.swing.panel.collection.table.renderer;

import java.awt.Component;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
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
    //Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    label.setToolTipText(buildHtmlTooltip(value + ""));
    
    @SuppressWarnings("unchecked")
    EventTableModel<Alert> model = (EventTableModel<Alert>) table.getModel();
    Alert e = model.getElementAt(row);
    Severity sev = e.getSeverity();
    if (isSelected) {
      // TODO: show the selected colors
      // c.setForeground(sev.getSelectionForegroundColor());
      // c.setBackground(sev.getSelectionBackgroundColor());
    } else {
    	label.setForeground(sev.getForegroundColor());
    	label.setBackground(sev.getBackgroundColor());
    }

    return label;
  }
  
  private HashMap<String,String> tooltipHash = new HashMap<String,String>();
  private String buildHtmlTooltip(String val)
  {
  	String strval = tooltipHash.get(val);
  	if (strval == null)
  	{
  	int maxchars = 100;
  	StringBuffer buf = new StringBuffer();
  	buf.append("<html>");
  	
  	for (int i=0; i<val.length(); i++)
  	{
  		char next = val.charAt(i);
  		buf.append(next);
  		if ((i % maxchars == 0) && (i != 0))
  		{
  			// line break
  			buf.append("<BR>");
  		}
  	}
  	
  	buf.append("</html>");
  	
  	strval = buf.toString();
  	tooltipHash.put(val, strval);
    if (tooltipHash.size() > 50000)
    {
    	tooltipHash.clear();
    }
  	}
  	return strval;
  }
  
}
