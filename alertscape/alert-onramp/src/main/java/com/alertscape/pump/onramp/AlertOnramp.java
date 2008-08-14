/**
 * 
 */
package com.alertscape.pump.onramp;

import java.util.HashMap;
import java.util.Map;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 *
 */
public class AlertOnramp {
	private long alertId;
	private AlertEquator equator;
	private Map<AlertSourceAlertEquator, Alert> alertMap = new HashMap<AlertSourceAlertEquator, Alert>();
	
	protected final void sendAlert(Alert alert) {
		AlertSourceAlertEquator alertWrapper = new AlertSourceAlertEquator(equator, alert);
		Alert existing = alertMap.get(alertWrapper);
		if(existing != null) {
			alert.setAlertId(existing.getAlertId());
			alert.setCount(existing.getCount()+1);
		} else {
			alert.setAlertId(alertId++);
		}
	}
}
