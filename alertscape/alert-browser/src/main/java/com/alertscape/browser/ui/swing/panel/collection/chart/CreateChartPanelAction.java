package com.alertscape.browser.ui.swing.panel.collection.chart;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import com.alertscape.browser.ui.swing.AlertBrowser;
import com.alertscape.common.model.Alert;

public class CreateChartPanelAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;
	private JFrame parentFrame;
	private ImageIcon myIcon;
	private ImageIcon chartIcon;

	public CreateChartPanelAction()
	{	
		super();
		putValue(NAME, getActionTitle());
		putValue(SMALL_ICON, getActionIcon());
		putValue(SHORT_DESCRIPTION, getActionDescription());
		putValue(MNEMONIC_KEY, getActionMnemonic());
		putValue(ACCELERATOR_KEY, getActionAccelerator());	
		
		URL imageUrl = getClass().getResource("/com/alertscape/images/mini/page_component.gif");
		chartIcon = new ImageIcon(imageUrl);
	}
	
	public void setParentFrame(JFrame parentFrame)
	{
		this.parentFrame = parentFrame;
	}

	public void actionPerformed(ActionEvent arg0)
	{
		// TODO Auto-generated method stub
		Thread t = new Thread() 
		{
			public void run()
			{
				// create a new pie chart panel with the alerts that are selected currently in the table
				List<Alert> alerts = AlertBrowser.getCurrentContext().getSelectedAlerts();
				if (alerts.size() > 0)
				{
					String title = "Chart";
					String tooltip = "Pie Chart (item, " + alerts.size() + ")";
					AlertPieChartPanel piechart = new AlertPieChartPanel(alerts, "item", title, tooltip);
											
					// add the chart to a tab, adjacent to the tabular view
					AlertBrowser.addTabbedPanel(title, chartIcon, piechart, tooltip, true);
				}
			}
		};
		t.start();		
	}	
	

	protected KeyStroke getActionAccelerator()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK);
	}


	protected String getActionDescription()
	{
		return "Chart Selected Alerts";
	}


	protected Icon getActionIcon()
	{
		if (myIcon == null)
		{
			URL imageUrl = getClass().getResource("/com/alertscape/images/mini/icon_component.gif");
		    ImageIcon icon = new ImageIcon(imageUrl);
		    myIcon = icon;	
		}
		
		return myIcon;
	}


	protected int getActionMnemonic()
	{
		return KeyEvent.VK_H;
	}


	protected String getActionTitle()
	{
		return "Chart Selected Alerts";
	}


	protected String getActionWindowTitle()
	{
		return "Chart Selected Alerts";
	}
}
