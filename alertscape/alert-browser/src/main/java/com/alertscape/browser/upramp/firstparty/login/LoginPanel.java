package com.alertscape.browser.upramp.firstparty.login;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.alertscape.browser.model.BrowserContext;
import com.alertscape.browser.upramp.model.AbstractUpRampPanel;
import com.alertscape.browser.upramp.model.UpRamp;

public class LoginPanel extends AbstractUpRampPanel
{
	private JSeparator buttonSeparator;
	private JTextField userField;
	private JLabel userLabel;
	private JPasswordField passwordField;
	private JLabel passwordLabel;
	private JButton sendButton;
	private JButton cancelButton;	
	
	private boolean sendPressed = false;
	
	public LoginPanel(BrowserContext bcontext, UpRamp ramp)
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
		map.put(LoginConstants.USER_ID, userField.getText());
		map.put(LoginConstants.PASSWORD, passwordField.getText());
		
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
		// assuming that there is no reason we would get any config from the server, I will not pull anything from the map
		
		// step 1: build out the ui
		BoxLayout mainbox = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(mainbox);
		
		JPanel useridPanel = new JPanel();
		BoxLayout userbox = new BoxLayout(useridPanel, BoxLayout.X_AXIS);
		useridPanel.setLayout(userbox);
		useridPanel.add(Box.createRigidArea(new Dimension(5,0)));
		userLabel = new JLabel("Username");
		userLabel.setToolTipText("The username you would like to login with");
		userLabel.setFocusable(false);
		userLabel.setPreferredSize(new Dimension(50, 20));
		useridPanel.add(userLabel);
		useridPanel.add(Box.createRigidArea(new Dimension(5,0)));
		userField = new JTextField();
		useridPanel.add(userField);
		useridPanel.add(Box.createRigidArea(new Dimension(5,0)));
				
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(useridPanel);

		JPanel passwordPanel = new JPanel();
		BoxLayout passwordbox = new BoxLayout(passwordPanel, BoxLayout.X_AXIS);
		passwordPanel.setLayout(passwordbox);
		passwordPanel.add(Box.createRigidArea(new Dimension(5,0)));
		passwordLabel = new JLabel("Password");
		passwordLabel.setToolTipText("The password for the userid you would like to login with");
		passwordLabel.setFocusable(false);
		passwordLabel.setPreferredSize(new Dimension(50, 20));
		passwordPanel.add(passwordLabel);
		passwordPanel.add(Box.createRigidArea(new Dimension(5,0)));
		passwordField = new JPasswordField();
		passwordPanel.add(passwordField);
		passwordPanel.add(Box.createRigidArea(new Dimension(5,0)));
		
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(passwordPanel);
		this.add(Box.createRigidArea(new Dimension(0,5)));
		
		buttonSeparator = new JSeparator();
		this.add(buttonSeparator);
		this.add(Box.createRigidArea(new Dimension(0,5)));
		
		JPanel buttonPanel = new JPanel();
		BoxLayout buttonbox = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
		buttonPanel.setLayout(buttonbox);
		buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));		
		cancelButton = new JButton("Cancel");
		cancelButton.setToolTipText("Cancel logging in");
		buttonPanel.add(cancelButton);
		buttonPanel.add(Box.createHorizontalGlue());
		sendButton = new JButton("Login");
		sendButton.setToolTipText("Login with this username/password"); 
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
