package com.alertscape.browser.upramp.firstparty.alertproperties;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.alertscape.browser.model.BrowserContext;
import com.alertscape.browser.upramp.firstparty.login.LoginConstants;
import com.alertscape.browser.upramp.model.AbstractUpRampPanel;
import com.alertscape.browser.upramp.model.UpRamp;
import com.alertscape.common.model.Alert;
import com.alertscape.common.util.AlertUtility;

import edu.emory.mathcs.backport.java.util.Collections;

public class AlertPropertiesPanel extends AbstractUpRampPanel
{
	private JSeparator buttonSeparator;
	private JButton sendButton = new JButton("Ok");
	private JButton cancelButton = new JButton("Cancel");
	private boolean sendPressed = false;

	public AlertPropertiesPanel(BrowserContext bcontext, UpRamp ramp)
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

		return map;
	}

	@Override
	public Dimension getBaseSize()
	{
		return new Dimension(620, 400);
	}

	private JPanel buildMiniPanel(String label, String labelTooltip, String val)
	{
		JPanel newPanel = new JPanel();
		JLabel newLabel = new JLabel(label);

		BoxLayout newbox = new BoxLayout(newPanel, BoxLayout.X_AXIS);
		newPanel.setLayout(newbox);
		newPanel.add(Box.createRigidArea(new Dimension(5, 0)));

		newLabel.setToolTipText(labelTooltip);
		newLabel.setFocusable(false);
		newLabel.setPreferredSize(new Dimension(90, 20));
		newLabel.setMinimumSize(new Dimension(90, 20));
		newPanel.add(newLabel);
		newPanel.add(Box.createRigidArea(new Dimension(5, 0)));

		if ((val == null) || (val.length() <= 80))
		{
			JTextField newField = new JTextField(val);
			newField.setText(val);
			newField.setToolTipText(val);
			//newField.setEnabled(false);
			newField.setEditable(false);
			newField.setBackground(Color.white);
			newPanel.add(newField);
			newPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		} else
		// line wrap
		{
			JTextPane newField = new JTextPane();

			int maxchars = 80;
			StringBuffer buf = new StringBuffer();

			for (int i = 0; i < val.length(); i++)
			{
				char next = val.charAt(i);
				buf.append(next);
				if ((i % maxchars == 0) && (i != 0))
				{
					// line break
					buf.append("\n");
				}
			}

			newField.setText(buf.toString());
			// newField.setToolTipText(buf.toString());
//			newField.setEnabled(false);
			newField.setEditable(false);
			newField.setBorder(BorderFactory.createLineBorder(Color.gray));
			newPanel.add(newField);
			newPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		}

		return newPanel;
	}

	private Component getHorizWhitespace()
	{
		return Box.createRigidArea(new Dimension(0, 3));
	}

	@Override
	protected boolean initialize(Map values)
	{
		// step 1: build out the ui
		BoxLayout thisbox = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(thisbox);

		// build the panel with all of the alert fields in it
		JPanel mainPanel = new JPanel();
		BoxLayout mainbox = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
		mainPanel.setLayout(mainbox);

		// get the first alert that is selected (only show the first right now
		Alert alert = getContext().getSelectedAlerts().get(0);

		mainPanel.add(getHorizWhitespace());

		mainPanel.add(buildMiniPanel(AlertUtility.labelAlertId, AlertUtility.labelAlertId, alert.getAlertId() + ""));
		mainPanel.add(getHorizWhitespace());

		mainPanel.add(buildMiniPanel(AlertUtility.labelFirstOccurrence, AlertUtility.labelFirstOccurrence, alert.getFirstOccurence() + ""));
		mainPanel.add(getHorizWhitespace());

		mainPanel.add(buildMiniPanel(AlertUtility.labelLastOccurrence, AlertUtility.labelLastOccurrence, alert.getLastOccurence() + ""));
		mainPanel.add(getHorizWhitespace());

		mainPanel.add(buildMiniPanel(AlertUtility.labelItem, AlertUtility.labelItem, alert.getItem()));
		mainPanel.add(getHorizWhitespace());

		mainPanel.add(buildMiniPanel(AlertUtility.labelItemType, AlertUtility.labelItemType, alert.getItemType()));
		mainPanel.add(getHorizWhitespace());

		mainPanel.add(buildMiniPanel(AlertUtility.labelManager, AlertUtility.labelManager, alert.getItemManager()));
		mainPanel.add(getHorizWhitespace());

		mainPanel.add(buildMiniPanel(AlertUtility.labelManagerType, AlertUtility.labelManagerType, alert.getItemManagerType()));
		mainPanel.add(getHorizWhitespace());

		mainPanel.add(buildMiniPanel(AlertUtility.labelShortDescription, AlertUtility.labelShortDescription, alert.getShortDescription()));
		mainPanel.add(getHorizWhitespace());

		mainPanel.add(buildMiniPanel(AlertUtility.labelLongDescription, AlertUtility.labelLongDescription, alert.getLongDescription()));
		mainPanel.add(getHorizWhitespace());

		mainPanel.add(buildMiniPanel("Type", "Type", alert.getType()));
		mainPanel.add(getHorizWhitespace());

		mainPanel.add(buildMiniPanel("Composite ID", "Composite ID", alert.getCompositeAlertId() + ""));
		mainPanel.add(getHorizWhitespace());

		mainPanel.add(buildMiniPanel(AlertUtility.labelCount, AlertUtility.labelCount, alert.getCount() + ""));
		mainPanel.add(getHorizWhitespace());

		mainPanel.add(buildMiniPanel(AlertUtility.labelSeverity, AlertUtility.labelSeverity, alert.getSeverity().getName()));
		mainPanel.add(getHorizWhitespace());

		mainPanel.add(buildMiniPanel(AlertUtility.labelSource, AlertUtility.labelSource, alert.getSource().getSourceName()));
		mainPanel.add(getHorizWhitespace());

		mainPanel.add(buildMiniPanel("Alert Status", "Alert Status", alert.getStatus().name()));
		mainPanel.add(getHorizWhitespace());

		mainPanel.add(buildMiniPanel("Acknowledged By", "Acknowledged By", alert.getAcknowledgedBy()));
		mainPanel.add(getHorizWhitespace());

		// extended attrs
		Map<String, Object> extAttrs = alert.getExtendedAttributes();
		ArrayList<String> sortedKeys = new ArrayList<String>(extAttrs.keySet().size());
		sortedKeys.addAll(extAttrs.keySet());
		Collections.sort(sortedKeys);
		for (int i = 0; i < sortedKeys.size(); i++)
		{
			String key = sortedKeys.get(i);
			String val = extAttrs.get(key) + "";
			mainPanel.add(buildMiniPanel(key, key, val));
			mainPanel.add(getHorizWhitespace());
		}

		// categories
		Map<String, Object> categories = alert.getMajorTags();
		sortedKeys = new ArrayList<String>(categories.keySet().size());
		sortedKeys.addAll(categories.keySet());
		Collections.sort(sortedKeys);
		for (int i = 0; i < sortedKeys.size(); i++)
		{
			String key = sortedKeys.get(i);
			String val = categories.get(key) + "";
			mainPanel.add(buildMiniPanel(key, key, val));
			mainPanel.add(getHorizWhitespace());
		}

		// labels
		Map<String, Object> labels = alert.getMinorTags();
		sortedKeys = new ArrayList<String>(labels.keySet().size());
		sortedKeys.addAll(labels.keySet());
		Collections.sort(sortedKeys);
		for (int i = 0; i < sortedKeys.size(); i++)
		{
			String key = sortedKeys.get(i);
			String val = labels.get(key) + "";
			mainPanel.add(buildMiniPanel(key, key, val));
			mainPanel.add(getHorizWhitespace());
		}

		this.add(new JScrollPane(mainPanel));

		JPanel buttonSeparatorPanel = new JPanel();
		BoxLayout buttonSepBoxLayout = new BoxLayout(buttonSeparatorPanel, BoxLayout.X_AXIS);
		buttonSeparatorPanel.setLayout(buttonSepBoxLayout);
		buttonSeparator = new JSeparator();
		buttonSeparatorPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		buttonSeparatorPanel.add(buttonSeparator);
		buttonSeparatorPanel.add(Box.createRigidArea(new Dimension(5, 0)));

		// this.add(buttonSeparatorPanel);
		// this.add(Box.createRigidArea(new Dimension(0, 5)));

		JPanel buttonPanel = new JPanel();
		BoxLayout buttonbox = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
		buttonPanel.setLayout(buttonbox);
		buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		cancelButton.setText("Cancel");
		cancelButton.setToolTipText("Close Alert Properties");
		buttonPanel.add(cancelButton);
		buttonPanel.add(Box.createHorizontalGlue());
		sendButton.setText("Ok");
		sendButton.setToolTipText("Close Alert Properties");
		buttonPanel.add(sendButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		sendButton.setNextFocusableComponent(cancelButton);

		// this.add(buttonPanel);
		// this.add(Box.createRigidArea(new Dimension(0, 5)));

		// add a listener to store that the send button was pressed
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				sendPressed = true;
			}
		});

		return true; // TODO: should do something better than just returning true
		// here
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
