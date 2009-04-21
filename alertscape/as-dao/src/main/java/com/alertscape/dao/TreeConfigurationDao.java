/**
 * 
 */
package com.alertscape.dao;

/**
 * @author josh
 *
 */
public interface TreeConfigurationDao {
  String getTreeConfiguration();
  
  void save(String configuration);
}
