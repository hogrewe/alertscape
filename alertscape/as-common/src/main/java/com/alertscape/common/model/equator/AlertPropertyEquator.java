/**
 * 
 */
package com.alertscape.common.model.equator;

import java.io.Serializable;

import com.alertscape.common.model.Alert;

interface AlertPropertyEquator extends Serializable {
	boolean equal(Alert a1, Alert a2);

	Object getHashableValue(Alert a);
}