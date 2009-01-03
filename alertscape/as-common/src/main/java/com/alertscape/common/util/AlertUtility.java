package com.alertscape.common.util;

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
	
	public static String toMultiLineKeyValString(Alert alert)
	{
		StringBuffer buf = new StringBuffer();
		
		buf.append(labelAlertId);
		buf.append(keyvalDelimiter);
		buf.append(alert.getAlertId());
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
		
		buf.append(tagsToMultiLineString(alert));
		
		buf.append(divider);
		
		return buf.toString();
	}
	
	public static String tagsToMultiLineString(Alert alert)
	{
		Map<String, Object> majors = alert.getMajorTags();		
		StringBuffer buf = new StringBuffer();
		buf.append(tagMapToMultiLineString(alert.getMajorTags()));
		buf.append(newline);
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
