/**
 * 
 */
package com.alertscape.browser.model.tree;

import org.apache.commons.lang.StringUtils;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class NonEmptyAttributeMatcher implements AlertMatcher {
  private String attributeName;

  public NonEmptyAttributeMatcher(String attributeName) {
    this.attributeName = attributeName;
  }
  
  public NonEmptyAttributeMatcher( ) {
    
  }

  public boolean matches(Alert alert) {
    Object attribute = alert.getExtendedAttribute(attributeName);
    
    if(attribute != null && !StringUtils.isEmpty(attribute.toString())) {
      return true;
    }
    
    attribute = alert.getMajorTag(attributeName);
    if(attribute != null && !StringUtils.isEmpty(attribute.toString())) {
      return true;
    }
    
    attribute = alert.getMinorTag(attributeName);
    if(attribute != null && !StringUtils.isEmpty(attribute.toString())) {
      return true;
    }
    
    return false;
  }

  /**
   * @return the attributeName
   */
  public String getAttributeName() {
    return attributeName;
  }

  /**
   * @param attributeName the attributeName to set
   */
  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

}
