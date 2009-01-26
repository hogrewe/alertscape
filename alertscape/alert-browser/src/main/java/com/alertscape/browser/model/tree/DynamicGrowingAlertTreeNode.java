/**
 * 
 */
package com.alertscape.browser.model.tree;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
  private static final Pattern attributePattern = Pattern.compile("cat\\{(\\w+)\\}");
  public static final char PATH_SEPARATOR = ':';

  private String dynamicPath;
  private String childPath;
  private Method childGetter;
  private String childField;
  private boolean usesAttribute;

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
      int sepIndex = dynamicPath.indexOf(PATH_SEPARATOR);
      // We have more children to conceive
      if (sepIndex > 0) {
        childPath = dynamicPath.substring(sepIndex + 1);
        childField = dynamicPath.substring(0, sepIndex);
      } else {
        childField = dynamicPath;
      }
      Matcher m = attributePattern.matcher(childField);
      if (m.matches()) {
        usesAttribute = true;
        childField = m.group(1);
      } else {
        childGetter = GetterHelper.makeEventGetter(childField);
      }
    }
  }

  @Override
  protected void addNonChildMatchingAlert(Alert alert) {
    try {
      if (childGetter == null && !usesAttribute) {
        return;
      }

      final Object value;
      ca.odell.glazedlists.matchers.Matcher<Alert> matcher;
      if (usesAttribute) {
        value = alert.getExtendedAttribute(childField);
        matcher = new AttributeAlertMatcher(childField, value);
      } else {
        value = childGetter.invoke(alert);
        matcher = Matchers.beanPropertyMatcher(Alert.class, childField, value);
      }
      
      // Don't add empty values
      if(value == null) {
        return;
      }

      DynamicGrowingAlertTreeNode child = new DynamicGrowingAlertTreeNode();
      child.setText(value.toString());
      child.setIcon(getIcon());
      child.setMatcher(matcher);
      child.setDynamicPath(childPath);
      int insertIndex = Collections.binarySearch(getChildren(), child, nodeComparator);
      addChild(child, -insertIndex - 1);
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
