package com.alertscape.browser.ui.swing.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

/**
 * 
 * @author Scott
 */
public class MailPanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	// Variables declaration - do not modify
	private JSeparator buttonSeparator;
	private JButton cancelButton = new JButton();
	private JTextField ccField;
	private JLabel ccLabel;
	private JTextField fromField;
	private JLabel fromLabel;
	private JLabel messageLabel;
	private JScrollPane messageScrollPane;
	private JSeparator messageSeparator;
	private JTextPane messageTextPane;
	private JButton sendButton = new JButton();
	private JTextField subjectField;
	private JLabel subjectLabel;
	private JTextField toField;
	private JLabel toLabel;

	private boolean sendPressed = false;

	// End of variables declaration

	/** Creates new form MailPanel */
	public MailPanel()
	{
	}

	@SuppressWarnings("deprecation")
	public void initComponents()
	{
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
		
		Dimension prefLabelSize = new Dimension(50, 20);
		Dimension vertRigid = new Dimension(0, 5);
		
		setMinimumSize(new Dimension(400, 300));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel fromPanel = new JPanel();
		BoxLayout frombox = new BoxLayout(fromPanel, BoxLayout.X_AXIS);
		fromPanel.setLayout(frombox);
		fromPanel.add(Box.createRigidArea(new Dimension(5,0)));
		fromLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fromLabel.setText("From");
		fromLabel.setToolTipText("The address which is sending the email");
		fromLabel.setFocusable(false);
		fromLabel.setPreferredSize(prefLabelSize);
		fromLabel.setMinimumSize(prefLabelSize);
		fromPanel.add(fromLabel);
		fromPanel.add(Box.createRigidArea(new Dimension(5,0)));
		fromField.setText("");
		fromField.setEnabled(false);
		fromField.setFocusable(false);
		fromPanel.add(fromField);
		fromPanel.add(Box.createRigidArea(new Dimension(5,0)));

		add(Box.createRigidArea(vertRigid));
		add(fromPanel);
		
		JPanel toPanel = new JPanel();
		BoxLayout tobox = new BoxLayout(toPanel, BoxLayout.X_AXIS);
		toPanel.setLayout(tobox);
		toPanel.add(Box.createRigidArea(new Dimension(5,0)));		
		toLabel.setHorizontalAlignment(SwingConstants.CENTER);
		toLabel.setText("To"); // NOI18N
		toLabel.setToolTipText("The address(es) that will receive this email, separated by semi colons"); // NOI18N
		toLabel.setFocusable(false);
		toLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		toLabel.setPreferredSize(prefLabelSize);
		toLabel.setMinimumSize(prefLabelSize);
		toPanel.add(toLabel);
		toPanel.add(Box.createRigidArea(new Dimension(5,0)));
		toField.setText(""); // NOI18N
		toField.setNextFocusableComponent(ccField);
		toPanel.add(toField);
		toPanel.add(Box.createRigidArea(new Dimension(5,0)));

		add(Box.createRigidArea(vertRigid));
		add(toPanel);
		
		JPanel ccPanel = new JPanel();
		BoxLayout ccbox = new BoxLayout(ccPanel, BoxLayout.X_AXIS);
		ccPanel.setLayout(ccbox);
		ccPanel.add(Box.createRigidArea(new Dimension(5,0)));		
		ccLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ccLabel.setText("CC"); // NOI18N
		ccLabel.setToolTipText("Additional address(es) that will receive this email, separated by semi colons"); // NOI18N
		ccLabel.setFocusable(false);
		ccLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		ccLabel.setPreferredSize(prefLabelSize);
		ccLabel.setMinimumSize(prefLabelSize);
		ccPanel.add(ccLabel);
		ccPanel.add(Box.createRigidArea(new Dimension(5,0)));
		ccField.setText(""); // NOI18N
		ccField.setNextFocusableComponent(subjectField);
		ccPanel.add(ccField);
		ccPanel.add(Box.createRigidArea(new Dimension(5,0)));

		add(Box.createRigidArea(vertRigid));
		add(ccPanel);
		
		JPanel subjectPanel = new JPanel();
		BoxLayout subjectbox = new BoxLayout(subjectPanel, BoxLayout.X_AXIS);
		subjectPanel.setLayout(subjectbox);
		subjectPanel.add(Box.createRigidArea(new Dimension(5,0)));	
		subjectLabel.setHorizontalAlignment(SwingConstants.CENTER);
		subjectLabel.setText("Subject"); // NOI18N
		subjectLabel.setToolTipText("The subject of this email message"); // NOI18N
		subjectLabel.setFocusable(false);
		subjectLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		subjectLabel.setPreferredSize(prefLabelSize);
		subjectLabel.setMinimumSize(prefLabelSize);
		subjectPanel.add(subjectLabel);
		subjectPanel.add(Box.createRigidArea(new Dimension(5,0)));
		subjectField.setText(""); // NOI18N
		subjectPanel.add(subjectField);
		subjectPanel.add(Box.createRigidArea(new Dimension(5,0)));
		
		add(Box.createRigidArea(vertRigid));
		add(subjectPanel);

		JPanel messageSeparatorPanel = new JPanel();
		BoxLayout msgSepBoxLayout = new BoxLayout(messageSeparatorPanel, BoxLayout.X_AXIS);
		messageSeparatorPanel.setLayout(msgSepBoxLayout);
		messageSeparatorPanel.add(Box.createRigidArea(new Dimension(5,0)));
		messageSeparatorPanel.add(messageSeparator);
		messageSeparatorPanel.add(Box.createRigidArea(new Dimension(5,0)));

		add(Box.createRigidArea(vertRigid));
		add(messageSeparatorPanel);
	
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
		messageLabel.setVerticalTextPosition(SwingConstants.TOP);
		messageLabel.setPreferredSize(prefLabelSize);
		messageLabel.setMinimumSize(prefLabelSize);
		main.add(messageLabel);
		main.add(Box.createRigidArea(new Dimension(5,0)));		
		messageScrollPane.setName("messageScrollPane"); // NOI18N
		messageTextPane.setMinimumSize(new Dimension(200, 200));
		messageTextPane.setName("messageTextPane"); // NOI18N
		messageTextPane.setPreferredSize(new Dimension(300, 400));
		messageScrollPane.setViewportView(messageTextPane);
		main.add(messageScrollPane);		
		main.add(Box.createRigidArea(new Dimension(5,0)));
		
		add(Box.createRigidArea(vertRigid));
		add(main);
		
		JPanel buttonSeparatorPanel = new JPanel();
		BoxLayout buttonSepBoxLayout = new BoxLayout(buttonSeparatorPanel, BoxLayout.X_AXIS);
		buttonSeparatorPanel.setLayout(buttonSepBoxLayout);
		buttonSeparatorPanel.add(Box.createRigidArea(new Dimension(5,0)));
		buttonSeparatorPanel.add(buttonSeparator);
		buttonSeparatorPanel.add(Box.createRigidArea(new Dimension(5,0)));
		
		add(Box.createRigidArea(vertRigid));
		add(buttonSeparatorPanel);
		
		JPanel bottom = new JPanel();
		BoxLayout bottombox = new BoxLayout(bottom, BoxLayout.X_AXIS);
		bottom.setLayout(bottombox);
		bottom.add(Box.createRigidArea(new Dimension(5,0)));
		cancelButton.setText("Cancel"); // NOI18N
		cancelButton.setToolTipText("Cancel sending this message"); // NOI18N
		cancelButton.setNextFocusableComponent(toField);
		bottom.add(cancelButton);
		bottom.add(Box.createHorizontalGlue());
		sendButton.setText("Send"); // NOI18N
		sendButton.setToolTipText("Send this email message"); // NOI18N
		sendButton.setNextFocusableComponent(cancelButton);
		bottom.add(sendButton);
		bottom.add(Box.createRigidArea(new Dimension(5,0)));

		add(Box.createRigidArea(vertRigid));
		add(bottom);
		add(Box.createRigidArea(vertRigid));
		
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
