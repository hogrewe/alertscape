package com.alertscape.browser.ui.swing.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;

import com.alertscape.browser.model.BrowserContext;
import com.alertscape.common.model.Alert;

public class AlertMailPanel extends JPanel {
	
	// static variables
	private static final long serialVersionUID = 1L;
	private static String defaultToEmailAddresses = "";		// the email addresses that should be defaulted into the to: field, semicolon delimited
	private static String defaultCCEmailAddresses = "";			// the email addresses that should be defaulted into the cc: field, semicolon delimited
	private static int MAX_SUBJECT_LENGTH = 260;			// max number of chars in the subject line	
	
		
	// public member variables
	public enum SubjectMode 
	{
		SHORT_DESC, LONG_DESC
	};
	public enum BodyMode 
	{
		KEY_VALS, TEMPLATE
	};
	
	// member variables
	private MailPanel mailpanel = new MailPanel();
	private SubjectMode subjectModeVal = SubjectMode.SHORT_DESC;
	private BodyMode bodyModeVal = BodyMode.KEY_VALS;
	
	// TODO: create a constructor that takes a list of Alert objects and initializes the composite panel with it based on how this panel is configured
	
	public AlertMailPanel(BrowserContext context)
	{
		// initialize the basics of the panels
		initComponentBasics();
		
		// build the from address
		String fromAddress = context.getCurrentUser().getEmail();
		
		// build the to address(es)
		String toAddress = defaultToEmailAddresses;
		
		// build the CC addresses
		String ccAddress = defaultCCEmailAddresses;
		
		// build the subject line
		String subject = buildSubject(context);
		
		// build the message body
		String body = buildMessageBody(context);
		
		// set all of the values into the mailpanel
		mailpanel.setFromFieldText(fromAddress);
		mailpanel.setToFieldText(toAddress);
		mailpanel.setCCFieldText(ccAddress);
		mailpanel.setSubjectFieldText(subject);
		mailpanel.setMessageFieldText(body);
	}
	
	private void initComponentBasics()
	{
		setMinimumSize(new Dimension(400, 300));	    
	    setLayout(new BorderLayout());
	    add(mailpanel, BorderLayout.CENTER);
	}

	private String buildSubject(BrowserContext context)
	{
		List<Alert> alerts = context.getSelectedAlerts();
		StringBuffer value = new StringBuffer();		
		Iterator<Alert> it = alerts.iterator();
		
		int count = 0;
		while (it.hasNext())
		{
			Alert a = it.next(); 
			count++;

			// TODO: this is not done, need to look at the modes and act accordingly

		}
		
		// truncate the subject line if it is too long
		if (value.length() > MAX_SUBJECT_LENGTH)
		{
			value.setLength(MAX_SUBJECT_LENGTH);
		}
		
		return value.toString();
	}
	
	private String buildMessageBody(BrowserContext context)
	{
		List<Alert> alerts = context.getSelectedAlerts();
		StringBuffer value = new StringBuffer();		
		Iterator<Alert> it = alerts.iterator();
		
		int count = 0;
		while (it.hasNext())
		{
			Alert a = it.next();
			count++;
			
			if (bodyModeVal == BodyMode.KEY_VALS)
			{
				String nextLine = buildKeyValString(a);
				value.append(nextLine);
			}
		}		
		
		//TODO: this is not done - needs key val suport, and templating support
		
		return value.toString();
	}
	
	private String buildKeyValString(Alert alert)
	{
		//TODO: fill in this method
		return "";
	}
	
	public static void setDefaultCCEmailAddresses(
			String defaultCCEmailAddresses) {
		AlertMailPanel.defaultCCEmailAddresses = defaultCCEmailAddresses;
	}

	public static String getDefaultCCEmailAddresses() {
		return defaultCCEmailAddresses;
	}

	public static void setDefaultToEmailAddresses(
			String defaultToEmailAddresses) {
		AlertMailPanel.defaultToEmailAddresses = defaultToEmailAddresses;
	}

	public static String getDefaultToEmailAddresses() {
		return defaultToEmailAddresses;
	}
}
