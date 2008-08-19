/**
 * 
 */
package com.alertscape.pump.onramp.equator;

import com.alertscape.common.model.Alert;

interface AlertPropertyEquator {
	boolean equal(Alert a1, Alert a2);

	Object getHashableValue(Alert a);
}