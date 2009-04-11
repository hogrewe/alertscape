/**
 * 
 */
package com.alertscape.dao;

import java.util.List;
import java.util.Map;

import com.alertscape.common.model.AlertAttributeDefinition;

/**
 * @author josh
 *
 */
public interface AlertAttributeDefinitionDao {
  List<AlertAttributeDefinition> getActiveDefinitions();
  void save(AlertAttributeDefinition definition);
  Map<String, Object> getActiveCategoryDefinitions();
  Map<String, List<String>> getActiveLabelDefinitions();
}
