/**
 * 
 */
package com.alertscape.pump.onramp.file;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author josh
 * 
 */
public class FileOnrampConfiguration {
  private String sourceName;
  private String fileName;
  private List<String> uniqueFields;
  private String regex;
  private String severityDeterminedField;
  private Map<String, String> fieldMappings;
  private Map<String, String> categoryMappings;
  private Map<Integer, List<String>> severityMappings;

  /**
   * @return the sourceName
   */
  public String getSourceName() {
    return sourceName;
  }

  /**
   * @param sourceName
   *          the sourceName to set
   */
  public void setSourceName(String sourceName) {
    this.sourceName = sourceName;
  }

  /**
   * @return the fileName
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * @param fileName
   *          the fileName to set
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * @return the uniqueFields
   */
  public List<String> getUniqueFields() {
    if (uniqueFields == null) {
      uniqueFields = Collections.emptyList();
    }
    return uniqueFields;
  }

  /**
   * @param uniqueFields
   *          the uniqueFields to set
   */
  public void setUniqueFields(List<String> uniqueFields) {
    this.uniqueFields = uniqueFields;
  }

  /**
   * @return the regex
   */
  public String getRegex() {
    return regex;
  }

  /**
   * @param regex
   *          the regex to set
   */
  public void setRegex(String regex) {
    this.regex = regex;
  }

  /**
   * @return the severityDeterminedField
   */
  public String getSeverityDeterminedField() {
    return severityDeterminedField;
  }

  /**
   * @param severityDeterminedField
   *          the severityDeterminedField to set
   */
  public void setSeverityDeterminedField(String severityDeterminedField) {
    this.severityDeterminedField = severityDeterminedField;
  }

  /**
   * @return the fieldMappings
   */
  public Map<String, String> getFieldMappings() {
    if (fieldMappings == null) {
      fieldMappings = Collections.emptyMap();
    }
    return fieldMappings;
  }

  /**
   * @param fieldMappings
   *          the fieldMappings to set
   */
  public void setFieldMappings(Map<String, String> fieldMappings) {
    this.fieldMappings = fieldMappings;
  }

  /**
   * @return the categoryMappings
   */
  public Map<String, String> getCategoryMappings() {
    if (categoryMappings == null) {
      categoryMappings = Collections.emptyMap();
    }
    return categoryMappings;
  }

  /**
   * @param categoryMappings
   *          the categoryMappings to set
   */
  public void setCategoryMappings(Map<String, String> categoryMappings) {
    this.categoryMappings = categoryMappings;
  }

  /**
   * @return the severityMappings
   */
  public Map<Integer, List<String>> getSeverityMappings() {
    return severityMappings;
  }

  /**
   * @param severityMappings the severityMappings to set
   */
  public void setSeverityMappings(Map<Integer, List<String>> severityMappings) {
    this.severityMappings = severityMappings;
  }
}
