/**
 * 
 */
package com.alertscape.dao;

import java.util.List;

import com.alertscape.common.model.AlertAttributeDefinition;

/**
 * @author josh
 *
 */
public interface AlertAttributeDefinitionDao {
  List<AlertAttributeDefinition> getActiveDefinitions();
  void save(AlertAttributeDefinition definition);
}
