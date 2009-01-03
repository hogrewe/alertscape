package com.alertscape.browser.upramp.firstparty.mail;

public class AlertMailConstants
{
	public static String SUBJECT_MODE = "subjectModelVal";
	public static String BODY_MODE = "bodyModeVal";
	public static String DEFAULT_TO_EMAILS = "defaultToEmailAddresses";
	public static String DEFAULT_CC_EMAILS = "defaultCCEmailAddresses";
	
	public static String SUBMIT_FROM_ADDRESSES = "fromAddresses";
	public static String SUBMIT_TO_ADDRESSES = "toAddresses";
	public static String SUBMIT_CC_ADDRESSES = "ccAddresses";
	public static String SUBMIT_SUBJECT = "subject";
	public static String SUBMIT_BODY = "body";
	
	
	// public member variables
	public enum SubjectMode 
	{
		SHORT_DESC, LONG_DESC
	};
	public enum BodyMode 
	{
		KEY_VALS, TEMPLATE
	};
}
