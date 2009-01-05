package com.alertscape.browser.upramp.firstparty.mail;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alertscape.browser.model.BrowserContext;
import com.alertscape.browser.ui.swing.panel.MailPanel;
import com.alertscape.browser.upramp.firstparty.mail.AlertMailConstants.BodyMode;
import com.alertscape.browser.upramp.firstparty.mail.AlertMailConstants.SubjectMode;
import com.alertscape.browser.upramp.model.AbstractUpRampPanel;
import com.alertscape.browser.upramp.model.UpRamp;
import com.alertscape.common.model.Alert;
import com.alertscape.common.util.AlertUtility;

public class AlertMailPanel extends AbstractUpRampPanel {
	
	// static variables
	private static final long serialVersionUID = 1L;
	private static String defaultToEmailAddresses = "";		// the email addresses that should be defaulted into the to: field, semicolon delimited
	private static String defaultCCEmailAddresses = "";		// the email addresses that should be defaulted into the cc: field, semicolon delimited
	private static int MAX_SUBJECT_LENGTH = 260;			// max number of chars in the subject line	
	
	// member variables
	private MailPanel mailpanel = new MailPanel();
	private SubjectMode subjectModeVal = SubjectMode.SHORT_DESC;
	private BodyMode bodyModeVal = BodyMode.KEY_VALS;
	
	public Dimension getBaseSize()
	{
		return new Dimension(425, 350);
	}
	
	public BodyMode getBodyMode() {
		return bodyModeVal;
	}

	public void setBodyMode(BodyMode bodyModeVal) {
		this.bodyModeVal = bodyModeVal;
	}

	public AlertMailPanel(BrowserContext bcontext, UpRamp ramp)
	{
		// setup the member variables
		setContext(bcontext);
		setUpramp(ramp);
	}
	
	protected boolean initialize(Map values)
	{
		// initialize the basics of the panels
		initComponentBasics();
		
		// build the from address
		String fromAddress = getContext().getCurrentUser().getEmail();
		
		// build the to address(es)
		String toAddress = (String)values.get(AlertMailConstants.DEFAULT_TO_EMAILS);
		
		// build the CC addresses
		String ccAddress = (String)values.get(AlertMailConstants.DEFAULT_CC_EMAILS);
		
		// build the subject line
		String subject = buildSubject(values);
		
		// build the message body
		String body = buildMessageBody(values);
		
		// set all of the values into the mailpanel
		mailpanel.setFromFieldText(fromAddress);
		mailpanel.setToFieldText(toAddress);
		mailpanel.setCCFieldText(ccAddress);
		mailpanel.setSubjectFieldText(subject);
		mailpanel.setMessageFieldText(body);
		
		return true; // TODO: need to do something better than just return true
	}
	
	public void associateHideListener(ActionListener listener)
	{
		mailpanel.associateHideListener(listener);
	}
	
	public boolean needsSubmit()
	{
		return mailpanel.isSendPressed();
	}
	
	protected Map buildSubmitMap()
	{
		HashMap map = new HashMap();
		map.put(AlertMailConstants.SUBMIT_FROM_ADDRESSES, mailpanel.getFromFieldText());
		map.put(AlertMailConstants.SUBMIT_TO_ADDRESSES, mailpanel.getToFieldText());
		map.put(AlertMailConstants.SUBMIT_CC_ADDRESSES, mailpanel.getCCFieldText());
		map.put(AlertMailConstants.SUBMIT_BODY, mailpanel.getFromFieldText());
		
		return map;
	} 
	
	private void initComponentBasics()
	{
		mailpanel.initComponents();
		
	  setLayout(new BorderLayout());
	  add(mailpanel, BorderLayout.CENTER);
	}

	private String buildSubject(Map values)
	{
		List<Alert> alerts = getContext().getSelectedAlerts();		
		StringBuffer value = new StringBuffer();
		
		subjectModeVal = (SubjectMode)values.get(AlertMailConstants.SUBJECT_MODE);
		
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
	
	private String buildMessageBody(Map values)
	{
		List<Alert> alerts = getContext().getSelectedAlerts();
		String value = "No Alerts Selected";
		
		bodyModeVal = (BodyMode)values.get(AlertMailConstants.BODY_MODE);
		
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
