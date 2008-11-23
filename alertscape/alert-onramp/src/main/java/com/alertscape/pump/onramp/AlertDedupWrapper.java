/**
 * 
 */
package com.alertscape.pump.onramp;

import com.alertscape.common.model.Alert;
import com.alertscape.pump.onramp.equator.AlertEquator;

/**
 * @author josh
 * 
 */
public class AlertDedupWrapper {
  public AlertEquator equator;
  public Alert alert;

  public AlertDedupWrapper(AlertEquator equator, Alert alert) {
    this.equator = equator;
    this.alert = alert;
  }

  @Override
  public int hashCode() {
    return equator.hashAlert(alert);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    
    if(!(obj instanceof AlertDedupWrapper)) {
      return false;
    }
    
    AlertDedupWrapper dedup = (AlertDedupWrapper) obj;

    return equator.equal(alert, dedup.alert);
  }

}
