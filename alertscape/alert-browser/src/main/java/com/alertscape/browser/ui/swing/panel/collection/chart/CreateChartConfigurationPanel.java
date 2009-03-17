package com.alertscape.browser.ui.swing.panel.collection.chart;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.alertscape.browser.ui.swing.AlertBrowser;
import com.alertscape.common.util.AlertUtility;

public class CreateChartConfigurationPanel extends JPanel
{
	public static final String KEY_CHARTTYPE_PIE = "Pie Chart";
	public static final String KEY_CHARTTYPE_BAR = "Bar Chart";
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSeparator buttonSeparator;
	private JComboBox chartTypeField;
	private JLabel chartTypeLabel;
	private JComboBox chartAlertAttributeField;
	private JLabel chartAlertAttributeLabel;
	private JButton chartButton = new JButton();
	private JButton cancelButton = new JButton();	
	
	private boolean sendPressed = false;
	
	public CreateChartConfigurationPanel()
	{
		// setup the member variables
	}

	public void associateHideListener(ActionListener listener)
	{
		chartButton.addActionListener(listener);
		cancelButton.addActionListener(listener);
	}


	public Dimension getBaseSize()
	{
		return new Dimension(300, 125);
	}

	protected boolean initialize()
	{
		// step 1: build out the ui
		BoxLayout mainbox = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(mainbox);
		
		JPanel typePanel = new JPanel();
		BoxLayout userbox = new BoxLayout(typePanel, BoxLayout.X_AXIS);
		typePanel.setLayout(userbox);
		typePanel.add(Box.createRigidArea(new Dimension(5,0)));
		chartTypeLabel = new JLabel("Type");
		chartTypeLabel.setToolTipText("The type of chart to create");
		chartTypeLabel.setFocusable(false);
		chartTypeLabel.setPreferredSize(new Dimension(50, 20));
		chartTypeLabel.setMinimumSize(new Dimension(50, 20));
		typePanel.add(chartTypeLabel);
		typePanel.add(Box.createRigidArea(new Dimension(5,0)));
		
		// Create the list of all of the types of charts that are supported by the system for creation
		List chartTypes = new ArrayList();
		chartTypes.add(KEY_CHARTTYPE_PIE);
//		chartTypes.add(KEY_CHARTTYPE_BAR);
		chartTypeField = new JComboBox(chartTypes.toArray());
		chartTypeField.setEditable(false);
		
		typePanel.add(chartTypeField);
		typePanel.add(Box.createRigidArea(new Dimension(5,0)));
				
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(typePanel);

		JPanel attributePanel = new JPanel();
		BoxLayout passwordbox = new BoxLayout(attributePanel, BoxLayout.X_AXIS);
		attributePanel.setLayout(passwordbox);
		attributePanel.add(Box.createRigidArea(new Dimension(5,0)));
		chartAlertAttributeLabel = new JLabel("By");
		chartAlertAttributeLabel.setToolTipText("The alert data values to chart");
		chartAlertAttributeLabel.setFocusable(false);
		chartAlertAttributeLabel.setPreferredSize(new Dimension(50, 20));
		chartAlertAttributeLabel.setMinimumSize(new Dimension(50, 20));
		attributePanel.add(chartAlertAttributeLabel);
		attributePanel.add(Box.createRigidArea(new Dimension(5,0)));
	
		
	  // Create the list of all of the alert attributes that are supported by the system for chart creation
		List<String> chartAttrs = new ArrayList();
  	// add all of the static alert fields
		chartAttrs.add(AlertUtility.labelItem);
		chartAttrs.add(AlertUtility.labelItemType);
		chartAttrs.add(AlertUtility.labelManager);
		chartAttrs.add(AlertUtility.labelManagerType);		
		chartAttrs.add(AlertUtility.labelType);
		chartAttrs.add(AlertUtility.labelCount);
		chartAttrs.add(AlertUtility.labelSeverity);
		chartAttrs.add(AlertUtility.labelShortDescription);
		chartAttrs.add(AlertUtility.labelLongDescription);
		chartAttrs.add(AlertUtility.labelSource);		
		chartAttrs.addAll(AlertUtility.getAllDynamicKeys(AlertBrowser.getCurrentContext().getSelectedAlerts()));

		chartAlertAttributeField = new JComboBox(chartAttrs.toArray());
		chartAlertAttributeField.setEditable(false);
		
		attributePanel.add(chartAlertAttributeField);
		attributePanel.add(Box.createRigidArea(new Dimension(5,0)));			
		
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(attributePanel);
		this.add(Box.createRigidArea(new Dimension(0,5)));
		
		JPanel buttonSeparatorPanel = new JPanel();
		BoxLayout buttonSepBoxLayout = new BoxLayout(buttonSeparatorPanel, BoxLayout.X_AXIS);
		buttonSeparatorPanel.setLayout(buttonSepBoxLayout);
		buttonSeparator = new JSeparator();
		buttonSeparatorPanel.add(Box.createRigidArea(new Dimension(5,0)));
		buttonSeparatorPanel.add(buttonSeparator);
		buttonSeparatorPanel.add(Box.createRigidArea(new Dimension(5,0)));
		
		this.add(buttonSeparatorPanel);
		this.add(Box.createRigidArea(new Dimension(0,5)));
		
		JPanel buttonPanel = new JPanel();
		BoxLayout buttonbox = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
		buttonPanel.setLayout(buttonbox);
		buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));		
		cancelButton.setText("Cancel");
		cancelButton.setToolTipText("Cancel creating chart");
		buttonPanel.add(cancelButton);
		buttonPanel.add(Box.createHorizontalGlue());
		chartButton.setText("Chart");
		chartButton.setToolTipText("Create chart as configured"); 
		buttonPanel.add(chartButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
		
		this.add(buttonPanel);
		this.add(Box.createRigidArea(new Dimension(0,5)));
		
		// add a listener to store that the send button was pressed
		chartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				sendPressed = true;
			}
		});
		
		return true;  // TODO: should do something better than just returning true here
	}

	public boolean needsSubmit()
	{
		return sendPressed;
	}

	public void setSendPressed(boolean sendPressed)
	{
		this.sendPressed = sendPressed;
	}

	public boolean isSendPressed()
	{
		return sendPressed;
	}
	
	public String getChartType()
	{
		return chartTypeField.getSelectedItem().toString();
	}
	
	public String getChartAttribute()
	{
		return chartAlertAttributeField.getSelectedItem().toString();
	}
}

