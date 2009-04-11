package com.alertscape.browser.upramp.firstparty.customtag;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.alertscape.browser.model.BrowserContext;
import com.alertscape.browser.upramp.model.AbstractUpRampPanel;
import com.alertscape.browser.upramp.model.UpRamp;
import com.alertscape.service.tags.CustomTagConstants;

public class CustomTagPanel extends AbstractUpRampPanel
{
	private JSeparator buttonSeparator;
	private JComboBox tagField;
	private JLabel tagLabel;
	private JTextField valueField;
	private JLabel valueLabel;
	private JButton sendButton = new JButton();
	private JButton cancelButton = new JButton();	
	
	private boolean sendPressed = false;
	
	public CustomTagPanel(BrowserContext bcontext, UpRamp ramp)
	{
		// setup the member variables
		setContext(bcontext);
		setUpramp(ramp);
	}

	@Override
	public void associateHideListener(ActionListener listener)
	{
		sendButton.addActionListener(listener);
		cancelButton.addActionListener(listener);
	}

	@Override
	protected Map buildSubmitMap()
	{
		HashMap map = new HashMap();
		map.put(CustomTagConstants.TAG_NAME, tagField.getSelectedItem());
		map.put(CustomTagConstants.TAG_VALUE, valueField.getText());
		
		return map;
	}

	@Override
	public Dimension getBaseSize()
	{
		return new Dimension(300, 125);
	}

	@Override
	protected boolean initialize(Map values)
	{
		if (values == null)
		{
			values = new HashMap();
			ArrayList vals = new ArrayList();
			values.put(CustomTagConstants.EXISTING_TAGNAMES, vals);
		}
		// step 1: build out the ui
		BoxLayout mainbox = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(mainbox);
		
		JPanel tagPanel = new JPanel();
		BoxLayout userbox = new BoxLayout(tagPanel, BoxLayout.X_AXIS);
		tagPanel.setLayout(userbox);
		tagPanel.add(Box.createRigidArea(new Dimension(5,0)));
		tagLabel = new JLabel("Name");
		tagLabel.setToolTipText("The name of the tag you would like to set on the alerts");
		tagLabel.setFocusable(false);
		tagLabel.setPreferredSize(new Dimension(50, 20));
		tagLabel.setMinimumSize(new Dimension(50, 20));
		tagPanel.add(tagLabel);
		tagPanel.add(Box.createRigidArea(new Dimension(5,0)));
		
		// get the list of all of the possible existing custom tags from the map
		List tagNames = (List)values.get(CustomTagConstants.EXISTING_TAGNAMES);
		
		if (tagNames == null)
		{
			JOptionPane.showMessageDialog(this.getParent(),
			    "Unable to load Labels from server",
			    "Loading Error",
			    JOptionPane.ERROR_MESSAGE);
      tagNames = new ArrayList();
      
      return false;
		}

		tagField = new JComboBox(tagNames.toArray());
		tagField.setEditable(true);
		
		tagPanel.add(tagField);
		tagPanel.add(Box.createRigidArea(new Dimension(5,0)));
				
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(tagPanel);

		JPanel valuePanel = new JPanel();
		BoxLayout passwordbox = new BoxLayout(valuePanel, BoxLayout.X_AXIS);
		valuePanel.setLayout(passwordbox);
		valuePanel.add(Box.createRigidArea(new Dimension(5,0)));
		valueLabel = new JLabel("Value");
		valueLabel.setToolTipText("The value you would like to set for the given tag on the selected alerts");
		valueLabel.setFocusable(false);
		valueLabel.setPreferredSize(new Dimension(50, 20));
		valueLabel.setMinimumSize(new Dimension(50, 20));
		valuePanel.add(valueLabel);
		valuePanel.add(Box.createRigidArea(new Dimension(5,0)));
		valueField = new JTextField();
		valuePanel.add(valueField);
		valuePanel.add(Box.createRigidArea(new Dimension(5,0)));
		
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(valuePanel);
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
		cancelButton.setToolTipText("Cancel custom tagging alerts");
		buttonPanel.add(cancelButton);
		buttonPanel.add(Box.createHorizontalGlue());
		sendButton.setText("Label");
		sendButton.setToolTipText("Tag selected alerts with name/value"); 
		buttonPanel.add(sendButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
		
		this.add(buttonPanel);
		this.add(Box.createRigidArea(new Dimension(0,5)));
		
		// add a listener to store that the send button was pressed
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				sendPressed = true;
			}
		});
		
		return true;  // TODO: should do something better than just returning true here
	}

	@Override
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
}
