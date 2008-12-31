package com.alertscape.browser.ui.swing.panel;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 * @author Scott
 */
public class MailPanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	// Variables declaration - do not modify
	private JSeparator buttonSeparator;
	private JButton cancelButton;
	private JTextField ccField;
	private JLabel ccLabel;
	private JTextField fromField;
	private JLabel fromLabel;
	private JLabel messageLabel;
	private JScrollPane messageScrollPane;
	private JSeparator messageSeparator;
	private JTextPane messageTextPane;
	private JButton sendButton;
	private JTextField subjectField;
	private JLabel subjectLabel;
	private JTextField toField;
	private JLabel toLabel;

	private boolean sendPressed = false;

	// End of variables declaration

	/** Creates new form MailPanel */
	public MailPanel()
	{
		initComponents();
	}

	@SuppressWarnings("deprecation")
	private void initComponents()
	{
		GridBagConstraints gridBagConstraints;

		fromLabel = new JLabel();
		fromField = new JTextField();
		toLabel = new JLabel();
		toField = new JTextField();
		ccLabel = new JLabel();
		ccField = new JTextField();
		subjectLabel = new JLabel();
		subjectField = new JTextField();
		messageSeparator = new JSeparator();
		messageLabel = new JLabel();
		messageScrollPane = new JScrollPane();
		messageTextPane = new JTextPane();
		buttonSeparator = new JSeparator();
		cancelButton = new JButton();
		sendButton = new JButton();
		
		setMinimumSize(new Dimension(400, 300));
		setName("Form"); // NOI18N
		//setLayout(new GridBagLayout());
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel top = new JPanel(new GridBagLayout());

		fromLabel.setHorizontalAlignment(SwingConstants.CENTER);
		// org.jdesktop.application.ResourceMap resourceMap =
		// org.jdesktop.application.Application.getInstance(desktopapplication1.DesktopApplication1.class).getContext().getResourceMap(MailPanel.class);
		fromLabel.setText("From"); // NOI18N
		fromLabel.setToolTipText("The address which is sending the email"); // NOI18N
		fromLabel.setFocusable(false);
		fromLabel.setName("fromLabel"); // NOI18N
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.ipadx = 5;
		gridBagConstraints.ipady = 5;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 0.1;
		gridBagConstraints.insets = new Insets(3, 0, 0, 0);
		top.add(fromLabel, gridBagConstraints);

		fromField.setText(""); // NOI18N
		fromField.setEnabled(false);
		fromField.setFocusable(false);
		fromField.setName("fromField"); // NOI18N
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 0.9;
		gridBagConstraints.insets = new Insets(3, 0, 0, 3);
		top.add(fromField, gridBagConstraints);

		toLabel.setHorizontalAlignment(SwingConstants.CENTER);
		toLabel.setText("To"); // NOI18N
		toLabel.setToolTipText("The address(es) that will receive this email, separated by semi colons"); // NOI18N
		toLabel.setFocusable(false);
		toLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		toLabel.setName("toLabel"); // NOI18N
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets(3, 0, 0, 0);
		top.add(toLabel, gridBagConstraints);

		toField.setText(""); // NOI18N
		toField.setName("toField"); // NOI18N
		toField.setNextFocusableComponent(ccField);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(3, 0, 0, 3);
		top.add(toField, gridBagConstraints);

		ccLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ccLabel.setText("CC"); // NOI18N
		ccLabel.setToolTipText("Additional address(es) that will receive this email, separated by semi colons"); // NOI18N
		ccLabel.setFocusable(false);
		ccLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		ccLabel.setName("ccLabel"); // NOI18N
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets(3, 0, 0, 0);
		top.add(ccLabel, gridBagConstraints);

		ccField.setText(""); // NOI18N
		ccField.setName("ccField"); // NOI18N
		ccField.setNextFocusableComponent(subjectField);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(3, 0, 0, 3);
		top.add(ccField, gridBagConstraints);

		subjectLabel.setHorizontalAlignment(SwingConstants.CENTER);
		subjectLabel.setText("Subject"); // NOI18N
		subjectLabel.setToolTipText("The subject of this email message"); // NOI18N
		subjectLabel.setFocusable(false);
		subjectLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		subjectLabel.setName("subjectLabel"); // NOI18N
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets(3, 0, 0, 0);
		top.add(subjectLabel, gridBagConstraints);

		subjectField.setText(""); // NOI18N
		subjectField.setName("subjectField"); // NOI18N
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(3, 0, 0, 3);
		top.add(subjectField, gridBagConstraints);

		messageSeparator.setName("bodySeparator"); // NOI18N
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.ipadx = 1;
		gridBagConstraints.ipady = 1;
		gridBagConstraints.insets = new Insets(6, 3, 3, 3);
		top.add(messageSeparator, gridBagConstraints);

		add(top);
		
		JPanel main = new JPanel();
		BoxLayout box = new BoxLayout(main, BoxLayout.X_AXIS);
		main.setLayout(box);
		
		main.add(Box.createRigidArea(new Dimension(5,0)));
		
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		messageLabel.setText("Message"); // NOI18N
		messageLabel.setToolTipText("The complete message that is to be sent"); // NOI18N
		messageLabel.setVerticalAlignment(SwingConstants.TOP);
		messageLabel.setFocusable(false);
		messageLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		messageLabel.setName("messageLabel"); // NOI18N
		messageLabel.setVerticalTextPosition(SwingConstants.TOP);
		main.add(messageLabel);

		main.add(Box.createRigidArea(new Dimension(5,0)));
		
		messageScrollPane.setName("messageScrollPane"); // NOI18N

		messageTextPane.setMinimumSize(new Dimension(200, 200));
		messageTextPane.setName("messageTextPane"); // NOI18N
		messageTextPane.setPreferredSize(new Dimension(300, 400));
		messageScrollPane.setViewportView(messageTextPane);

		main.add(messageScrollPane);
		
		main.add(Box.createRigidArea(new Dimension(5,0)));
		
		add(main);
		
		JPanel bottom = new JPanel();
		BoxLayout bottombox = new BoxLayout(bottom, BoxLayout.X_AXIS);
		bottom.setLayout(bottombox);
		
		
		JPanel buttonSeparatorPanel = new JPanel();
		BoxLayout buttonSepBoxLayout = new BoxLayout(buttonSeparatorPanel, BoxLayout.X_AXIS);
		buttonSeparatorPanel.setLayout(buttonSepBoxLayout);
		buttonSeparatorPanel.add(Box.createRigidArea(new Dimension(5,0)));
		buttonSeparatorPanel.add(buttonSeparator);
		buttonSeparatorPanel.add(Box.createRigidArea(new Dimension(5,0)));
		add(Box.createRigidArea(new Dimension(0,5)));
		add(buttonSeparatorPanel);
		add(Box.createRigidArea(new Dimension(0,5)));
		
		bottom.add(Box.createRigidArea(new Dimension(5,0)));
		
		cancelButton.setText("Cancel"); // NOI18N
		cancelButton.setToolTipText("Cancel sending this message"); // NOI18N
		cancelButton.setName("cancelButton"); // NOI18N
		cancelButton.setNextFocusableComponent(toField);
		bottom.add(cancelButton);

		bottom.add(Box.createHorizontalGlue());
		
		sendButton.setText("Send"); // NOI18N
		sendButton.setToolTipText("Send this email message"); // NOI18N
		sendButton.setName("sendButton"); // NOI18N
		sendButton.setNextFocusableComponent(cancelButton);
		bottom.add(sendButton);
		
		bottom.add(Box.createRigidArea(new Dimension(5,0)));

		add(bottom);
		
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				sendPressed = true;
			}
		});
	}

	public void setFromFieldText(String text)
	{
		fromField.setText(text);
	}

	public void setToFieldText(String text)
	{
		toField.setText(text);
	}

	public void setCCFieldText(String text)
	{
		ccField.setText(text);
	}

	public void setSubjectFieldText(String text)
	{
		subjectField.setText(text);
	}

	public void setMessageFieldText(String text)
	{
		messageTextPane.setText(text);
	}

	public String getFromFieldText()
	{
		return fromField.getText();
	}

	public String getToFieldText()
	{
		return toField.getText();
	}

	public String getCCFieldText()
	{
		return ccField.getText();
	}

	public String getSubjectFieldText()
	{
		return subjectField.getText();
	}

	public String getMessageFieldText()
	{
		return messageTextPane.getText();
	}

	public void setSendPressed(boolean sendPressed)
	{
		this.sendPressed = sendPressed;
	}

	public boolean isSendPressed()
	{
		return sendPressed;
	}

	public void associateHideListener(ActionListener listener)
	{
		sendButton.addActionListener(listener);
		cancelButton.addActionListener(listener);
	}
}
