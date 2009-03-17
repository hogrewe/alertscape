package com.alertscape.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alertscape.common.model.Alert;

public class AlertUtility {

	// label names for the different alert fields
	public static final String labelLastOccurrence = "Last Event"; 
	public static final String labelFirstOccurrence = "First Event"; 
	public static final String labelType = "Type";
	public static final String labelItem = "Item"; 
	public static final String labelSeverity = "Severity"; 
	public static final String labelLongDescription = "Description";
	public static final String labelShortDescription = "Short Description";
	public static final String labelCount = "Count";
	public static final String labelManager = "Manager"; 
	public static final String labelItemType = "Item Type";
	public static final String labelManagerType = "Manager Type"; 
	public static final String labelAlertId = "Alert ID";
	public static final String labelSource = "Source";
	public static final String labelMajorTags = "Major Tags";
	public static final String labelMinorTags = "Minor Tags";
	
	private static final String keyvalDelimiter = "=";
	private static final String singleLineDelimiter = ", ";
	private static final String newline = "\n";
	private static final String divider = "------------------------------------------";
	
	public static Map<String,Integer> countAlertFields(List<Alert> alerts, String field)
	{
		// create a map of field value to counted occurances
  	Map<String,Integer> fieldVals = new HashMap<String, Integer>();
  	
  	// iterate through the alets, counting those fields
  	for (int i = 0; i < alerts.size(); i++)
  	{
  		// get the next alert
  		Alert alert = alerts.get(i);
  		
  		// get the next field value
  		String val = getAlertFieldValueFromAttributeName(alert, field);
  		
  		// get the current count, and increment it in the map
  		Integer itemCount = fieldVals.get(val);
  		if (itemCount == null)
  		{
  			itemCount = new Integer(0);
  		}
  		int newCount = itemCount.intValue() + 1;
  		fieldVals.put(val, new Integer(newCount));
  	}
  	
  	return fieldVals;
	}
	
	public static String getAlertFieldValueFromAttributeName(Alert alert, String name)
	{
		String value = null;
		// TODO - add all of the static fields here
		if (name.equals(labelItem)) 
		{
			value = alert.getItem();
		}		
		else if (name.equals(labelItemType)) 
		{
			value = alert.getItemType();
		}
		else if (name.equals(labelManager)) 
		{
			value = alert.getItemManager();
		}
		else if (name.equals(labelManagerType)) 
		{
			value = alert.getItemManagerType();
		}		
		else if (name.equals(labelSeverity)) 
		{
			value = alert.getSeverity().getName();
		}		
		else if (name.equals(labelType)) 
		{
			value = alert.getType();
		}
		else if (name.equals(labelCount)) 
		{
			value = alert.getCount() + "";
		}
		else if (name.equals(labelSource)) 
		{
			value = alert.getSource().getSourceName();
		}
		else if (name.equals(labelShortDescription)) 
		{
			value = alert.getShortDescription();
		}
		else if (name.equals(labelLongDescription)) 
		{
			value = alert.getLongDescription();
		}
		else if (name.equals(labelFirstOccurrence)) 
		{
			value = alert.getFirstOccurence().toString();
		}
		else if (name.equals(labelLastOccurrence)) 
		{
			value = alert.getLastOccurence().toString();
		}
		else if (name.equals(labelAlertId)) 
		{
			value = alert.getAlertId() + "";
		}		
		else // did not find a static field with this name, so try a major or minor tag (category/label)
		{
			// check if this field is a major tag...
			Object majorval = alert.getMajorTag(name);
			if (majorval != null)
			{
				value = majorval.toString();
			}
			else // it was not a major tag, so check if it is a minor tag
			{
				Object minorval = alert.getMinorTag(name);
				if (minorval != null)
				{
					value = minorval.toString();
				}
				else // if was not a static, major, or minor attribute...
				{
					value = "n/a";
				}
			}
		}
		
		// one last check, if the attribute was right, but the value was null, then reset the value to n/a
		if (value == null)
		{
			value = "n/a"; 
		}
		
		return value;
	}
	
	public static String toMultiLineKeyValString(Alert alert)
	{
		StringBuffer buf = new StringBuffer();
		
		buf.append(labelAlertId);
		buf.append(keyvalDelimiter);
		buf.append(alert.getCompositeAlertId());
		buf.append(newline);
		
		buf.append(divider);
		buf.append(newline);
		
		buf.append(labelShortDescription);
		buf.append(keyvalDelimiter);
		buf.append(alert.getShortDescription());
		buf.append(newline);
		
		buf.append(labelItem);
		buf.append(keyvalDelimiter);
		buf.append(alert.getItem());
		buf.append(newline);
		
		buf.append(labelLastOccurrence);
		buf.append(keyvalDelimiter);
		buf.append(alert.getLastOccurence());
		buf.append(newline);
		
		buf.append(labelFirstOccurrence);
		buf.append(keyvalDelimiter);
		buf.append(alert.getFirstOccurence());
		buf.append(newline);
		
		buf.append(labelItemType);
		buf.append(keyvalDelimiter);
		buf.append(alert.getItemType());
		buf.append(newline);
		
		buf.append(labelManager);
		buf.append(keyvalDelimiter);
		buf.append(alert.getItemManager());
		buf.append(newline);
		
		buf.append(labelManagerType);
		buf.append(keyvalDelimiter);
		buf.append(alert.getItemManagerType());
		buf.append(newline);
		
		buf.append(labelLongDescription);
		buf.append(keyvalDelimiter);
		buf.append(alert.getLongDescription());
		buf.append(newline);
		
		buf.append(labelType);
		buf.append(keyvalDelimiter);
		buf.append(alert.getType());
		buf.append(newline);
		
		buf.append(labelCount);
		buf.append(keyvalDelimiter);
		buf.append(alert.getCount());
		buf.append(newline);
		
		buf.append(labelSeverity);
		buf.append(keyvalDelimiter);
		buf.append(alert.getSeverity());
		buf.append(newline);
		
		buf.append(labelSource);
		buf.append(keyvalDelimiter);
		buf.append(alert.getSource());
		buf.append(newline);
		
		buf.append(divider);
		buf.append(newline);
		
		String tags = tagsToMultiLineString(alert);
		if (tags.length() > 0)
		{
			buf.append(tags);
			buf.append(newline);
		}
		
		buf.append(divider);
		buf.append(newline);
		
		return buf.toString();
	}
	
	public static String tagsToMultiLineString(Alert alert)
	{	
		StringBuffer buf = new StringBuffer();
		buf.append(tagMapToMultiLineString(alert.getMajorTags()));
		if (buf.length() > 0)
		{
			buf.append(newline);
		}
		buf.append(tagMapToMultiLineString(alert.getMinorTags()));
		
		return buf.toString();
	}
	
	public static final String tagMapToMultiLineString(Map<String, Object> map)
	{
		Set<String> keys = map.keySet();		
		Iterator<String> it = keys.iterator();
		
		StringBuffer buf = new StringBuffer();
		
		while (it.hasNext())
		{
			String tag = it.next();
			String val = map.get(tag).toString();
			buf.append(tag);
			buf.append(keyvalDelimiter);
			buf.append(val);
			if (it.hasNext())
			{
				buf.append(newline);
			}
		}
		
		return buf.toString();
	}
	
	public static final String alertListToMultiLineString(List<Alert> alerts)
	{			
		Iterator<Alert> it = alerts.iterator();
		StringBuffer value = new StringBuffer();

		int count = 0;
		while (it.hasNext())
		{
			// snag the next alert that is selected
			Alert a = it.next();
			count++;

			// build a keyvalue pair string, and add it to the body for each alert
			String nextLine = AlertUtility.toMultiLineKeyValString(a);
			value.append(nextLine);
		}
		// prepend the number of alerts that was selected, if it was greater than 1
		if (count>1)
		{
			StringBuffer newval = new StringBuffer(value.length());
			newval.append(count);
			newval.append(" Alerts\n");
			newval.append(value.toString());
			newval.append(divider);
			value = newval;
		}
			
		return value.toString();		
	}
	
	public static final String alertListToSingleLineFieldString(String fieldName, List<Alert> alerts)
	{			
		Iterator<Alert> it = alerts.iterator();
		StringBuffer value = new StringBuffer();
		
		while (it.hasNext())
		{
			// snag the next alert that is selected
			Alert a = it.next();
			
			if (fieldName.equals(labelShortDescription))
			{
				value.append(a.getShortDescription());
			}
			else if (fieldName.equals(labelLongDescription))
			{
				value.append(a.getLongDescription());
			}
			else
			{
				// try a major or minor tag
				Object majorval = a.getMajorTag(fieldName);
				if (majorval != null)
				{
					value.append(majorval.toString());
				}
				else
				{
					Object minorval = a.getMinorTag(fieldName);
					if (minorval != null)
					{
						value.append(minorval.toString());
					}
					else
					{
						value.append("n/a");
					}
				}
			}
			
			// if there are any more after this one, then add a delimiter
			if (it.hasNext())
			{
				value.append(singleLineDelimiter);
			}
			
		} // end loop over alerts
			
		return value.toString();		
	}
}
