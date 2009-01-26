/**
 * 
 */
package com.alertscape.pump;

import java.util.List;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 *
 */
public interface AlertSourceCallback {
  void updateAlert(Alert alert);
  List<Alert> getStanding();
}
