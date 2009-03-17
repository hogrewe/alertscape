package com.alertscape.browser.ui.swing.panel.collection.chart;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.alertscape.browser.localramp.model.AbstractLocalRampPanel;
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
		Thread t = new Thread() {
			public void run()
			{
					final JDialog dlg = new JDialog(parentFrame);
					dlg.setTitle(getActionWindowTitle());
					dlg.setModal(true);
					dlg.toFront();
					dlg.setLayout(new BorderLayout());

					// create the panel with the latest browsercontext
					final CreateChartConfigurationPanel panel = new CreateChartConfigurationPanel();

					dlg.setSize(panel.getBaseSize());
					dlg.setPreferredSize(panel.getBaseSize());

					// center within the parent
					dlg.setLocationRelativeTo(parentFrame);

					// add a hide listener to the panel
					ActionListener hider = new ActionListener() {
						public void actionPerformed(ActionEvent arg0)
						{
							Thread innerT = new Thread() 
							{
								public void run()
								{
									// Hide the dialog
									dlg.setVisible(false);

									// check if the panel needs to be submit
									if (panel.needsSubmit())
									{										
									  // create a new chart panel with the alerts that are selected currently in the table
									  List<Alert> alerts = AlertBrowser.getCurrentContext().getSelectedAlerts();
									  if (alerts.size() > 0)
									  {	
									  	String type = panel.getChartType();
									  	String attribute = panel.getChartAttribute();
									  	
 									    // create the chart based on their responses
										  String title = "Chart";
										  String tooltip = type + " by " + attribute + ", " + alerts.size() + " alerts";
										  										  
										  JPanel chart = null;
										  
										  if (type.equals(panel.KEY_CHARTTYPE_PIE))
										  {										  
										  	chart = new AlertPieChartPanel(alerts, attribute, title, tooltip);										  
										  }
										  else if (type.equals(panel.KEY_CHARTTYPE_BAR))
										  {										  
										  	//chart = new AlertPieChartPanel(alerts, attribute, title, tooltip);										  
										  }
										  
										  if (chart != null)
										  {
										  	// add the chart to a tab, adjacent to the tabular view
										  	AlertBrowser.addTabbedPanel(title, chartIcon, chart, tooltip, true, true);
										  }
									  }
									}
								}
							};
							innerT.start();
						}
					};

					panel.associateHideListener(hider);

					// initialize the panel using the upramp
					boolean initialized = panel.initialize();

					// make sure initialization worked
					if (initialized)
					{
						// add the panel to the dialog
						dlg.add(panel, BorderLayout.CENTER);

						// set the dialog as visible: // TODO: not sure if will be blocking
						// method until hide...
						dlg.setVisible(true);
					} else
					{
						// TODO: Pop an error message
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
