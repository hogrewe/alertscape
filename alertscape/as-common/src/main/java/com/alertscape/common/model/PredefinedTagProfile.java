package com.alertscape.common.model;

import java.io.Serializable;
import java.util.List;

public class PredefinedTagProfile implements Serializable	
{
	// TODO: this class does not belong in the package, but for now it is here.
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean userModifiable = false;
	private String defaultValue;
	private List<String> validValues;
	
	public void setUserModifiable(boolean userModifiable)
	{
		this.userModifiable = userModifiable;
	}
	
	public boolean isUserModifiable()
	{
		return userModifiable;
	}
	
	public void setDefaultValue(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}
	
	public String getDefaultValue()
	{
		return defaultValue;
	}
	
	public void setValidValues(List<String> validValues)
	{
		this.validValues = validValues;
	}
	
	public List<String> getValidValues()
	{
		return validValues;
	}
	
}
