package com.alertscape.pump.offramp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.alertscape.AlertscapeException;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertStatus;

public class MemoryOfframp implements AlertOfframp
{
	HashMap<Long, Alert> alertIdHash = new HashMap<Long, Alert>();
	
	public MemoryOfframp(List<Alert> alerts)
	{
		// build a hash of all of the alerts in memory
		Iterator<Alert> it = alerts.iterator();
		while (it.hasNext())
		{
			Alert a = it.next();
			alertIdHash.put(a.getAlertId(), a);
		}		
	}
	
	// purpose of this offramp is to maintain a real-time in-memory state of all alarms
	@Override
	public String getOfframpName()
	{
		return "MemoryOfframp";
	}

	@Override
	public void processAlert(Alert alert) throws AlertscapeException
	{				
		if (alert.getStatus().equals(AlertStatus.CLEARED))
		{
			alertIdHash.remove(alert.getAlertId());
		}
		else
		{
			alertIdHash.put(alert.getAlertId(), alert);
		}
	}
	
	public Alert getAlert(long alertId)
	{
		return alertIdHash.get(alertId);
	}
}
