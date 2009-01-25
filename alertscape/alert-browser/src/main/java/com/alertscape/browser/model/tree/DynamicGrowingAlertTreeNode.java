/**
 * 
 */
package com.alertscape.browser.model.tree;

import java.lang.reflect.Method;
import java.util.Comparator;

import ca.odell.glazedlists.matchers.Matchers;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.util.GetterHelper;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author josh
 * 
 */
public class DynamicGrowingAlertTreeNode extends DefaultAlertTreeNode {

  private static final ASLogger LOG = ASLogger.getLogger(DynamicGrowingAlertTreeNode.class);
  private static final AlphabeticalNodeComparator nodeComparator = new AlphabeticalNodeComparator();
  private String dynamicPath;
  private String childPath;
  private Method childGetter;
  private String childField;

  /**
   * @return the dynamicPath
   */
  public String getDynamicPath() {
    return dynamicPath;
  }

  /**
   * @param dynamicPath
   *          the dynamicPath to set
   */
  public void setDynamicPath(String dynamicPath) {
    this.dynamicPath = dynamicPath;
    if (dynamicPath != null && dynamicPath.length() > 0) {
      int dotIndex = dynamicPath.indexOf('.');
      // We have more children to conceive
      if (dotIndex > 0) {
        childPath = dynamicPath.substring(dotIndex + 1);
        childField = dynamicPath.substring(0, dotIndex);
      } else {
        childField = dynamicPath;
      }
      childGetter = GetterHelper.makeEventGetter(childField);
    }
  }

  @Override
  protected void addNonChildMatchingAlert(Alert alert) {
    try {
      if (childGetter == null) {
        return;
      }
      Object value = childGetter.invoke(alert);
      DynamicGrowingAlertTreeNode child = new DynamicGrowingAlertTreeNode();
      child.setText(value.toString());
      child.setIcon(getIcon());
      child.setMatcher(Matchers.beanPropertyMatcher(Alert.class, childField, value));
      child.setDynamicPath(childPath);
      int insertIndex = Collections.binarySearch(getChildren(), child, nodeComparator);
      addChild(child, -insertIndex-1);
      child.addAlert(alert);
    } catch (Exception e) {
      LOG.error("Couldn't add child node", e);
    }
  }

  private static class AlphabeticalNodeComparator implements Comparator<AlertTreeNode> {
    public int compare(AlertTreeNode o1, AlertTreeNode o2) {
      return o1.getText().compareTo(o2.getText());
    }

  }
}
