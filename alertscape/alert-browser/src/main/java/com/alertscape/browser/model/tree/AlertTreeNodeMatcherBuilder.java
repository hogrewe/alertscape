/*
 * Created on Oct 24, 2006
 */
package com.alertscape.browser.model.tree;

/**
 * @author josh
 * @version $Version: $
 */
public class AlertTreeNodeMatcherBuilder {
  public AlertTreeNode buildTree() {
    AlertTreeNode root = new DefaultAlertTreeNode("All Alerts");
    
    
    
    return root;
  }
  
  protected AlertTreeNode buildGeographicalNodes() {
    AlertTreeNode geoRoot = new DefaultAlertTreeNode("Geography");
    
    
    
    return geoRoot;
  }
}
