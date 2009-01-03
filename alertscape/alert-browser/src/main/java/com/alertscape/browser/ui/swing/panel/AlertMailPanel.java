package com.alertscape.browser.ui.swing.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;

import com.alertscape.browser.model.BrowserContext;
import com.alertscape.common.model.Alert;
import com.alertscape.common.util.AlertUtility;

public class AlertMailPanel extends JPanel {
	
	// static variables
	private static final long serialVersionUID = 1L;
	private static String defaultToEmailAddresses = "";		// the email addresses that should be defaulted into the to: field, semicolon delimited
	private static String defaultCCEmailAddresses = "";		// the email addresses that should be defaulted into the cc: field, semicolon delimited
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
	
	public BodyMode getBodyMode() {
		return bodyModeVal;
	}

	public void setBodyMode(BodyMode bodyModeVal) {
		this.bodyModeVal = bodyModeVal;
	}

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
		
		if (subjectModeVal == SubjectMode.SHORT_DESC)
		{
			value.append(AlertUtility.alertListToSingleLineFieldString(AlertUtility.labelShortDescription, alerts));
		}
		else if (subjectModeVal == SubjectMode.LONG_DESC)
		{
			value.append(AlertUtility.alertListToSingleLineFieldString(AlertUtility.labelLongDescription, alerts));
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
		String value = "No Alerts Selected";
			
		// check what mode the panel is operating in, to decide how to build the message body
		if (bodyModeVal == BodyMode.KEY_VALS)
		{
			value = AlertUtility.alertListToMultiLineString(alerts);
		}
		//TODO: this is not done - needs templating mode support
		return value.toString();
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

	public void setSubjectMode(SubjectMode subjectModeVal) {
		this.subjectModeVal = subjectModeVal;
	}

	public SubjectMode getSubjectMode() {
		return subjectModeVal;
	}
}
