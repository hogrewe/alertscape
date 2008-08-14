/**
 * 
 */
package com.alertscape.pump.onramp;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 *
 */
public class AlertSourceAlertEquator {
	public AlertEquator equator;
	public Alert alert;
	
	public AlertSourceAlertEquator(AlertEquator equator, Alert alert) {
		this.equator = equator;
		this.alert = alert;
	}

	@Override
	public int hashCode() {
		return equator.hashAlert(alert);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Alert ? equator.equal(alert, (Alert)obj) : false;
	}
}
