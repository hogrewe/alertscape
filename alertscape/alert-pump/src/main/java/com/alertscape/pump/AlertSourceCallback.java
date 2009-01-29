/**
 * 
 */
package com.alertscape.pump;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 *
 */
public interface AlertSourceCallback {
  void updateAlert(Alert alert);
}
