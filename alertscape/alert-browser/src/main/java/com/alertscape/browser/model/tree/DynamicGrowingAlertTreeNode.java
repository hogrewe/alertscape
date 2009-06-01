/**
 * 
 */
package com.alertscape.browser.model.tree;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.common.util.GetterHelper;

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
   * 
   */
  public DynamicGrowingAlertTreeNode() {
    super();
  }

  /**
   * @param text
   */
  public DynamicGrowingAlertTreeNode(String text) {
    super(text);
  }

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
        childGetter = GetterHelper.makeGetter(childField);
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
      AlertMatcher matcher;
      if (usesAttribute) {
        Object tmpvalue = alert.getExtendedAttribute(childField);
        if (tmpvalue == null) // if it is not a extattr, check if it is a major tag (category)
        {
          tmpvalue = alert.getMajorTag(childField);

          if (tmpvalue == null) // if it is not a major tag, check if it is a minor tag (label)
          {
            tmpvalue = alert.getMinorTag(childField);
          }
        }
        value = tmpvalue;

        matcher = new AttributeAlertMatcher(childField, value);
      } else {
        value = childGetter.invoke(alert);
        matcher = new FieldAlertMatcher(childField, value);
      }

      // Don't add empty values
      if (value == null) {
        return;
      }

      DynamicGrowingAlertTreeNode child = new DynamicGrowingAlertTreeNode();
      child.setRemovable(true);
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
