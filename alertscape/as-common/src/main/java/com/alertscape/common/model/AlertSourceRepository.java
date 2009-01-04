/**
 * 
 */
package com.alertscape.common.model;

/**
 * @author josh
 * 
 */
public interface AlertSourceRepository {
  AlertSource getAlertSource(String name);
  AlertSource getAlertSource(int sourceId);
}
