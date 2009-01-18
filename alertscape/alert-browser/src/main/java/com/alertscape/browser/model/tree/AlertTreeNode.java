/**
 * 
 */
package com.alertscape.browser.model.tree;

import java.util.List;

import com.alertscape.browser.model.criterion.AlertCriterion;
import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public interface AlertTreeNode {

  public static final String CHILD_ADDED_PROP = "ChildAdded";
  public static final String CHILD_REMOVED_PROP = "ChildRemoved";
  public static final String DISPLAY_CHANGED_PROP = "DisplayChanged";
  public static final String ALERT_ADDED_PROP = "EventAdded";
  public static final String ALERT_REMOVED_PROP = "EventRemoved";

  /**
   * This method will look to see if the event should be added to this node, given this node's criteria, and will return
   * true if it was added, and false if it was not.
   * 
   * @param a
   *          The event to add to the node
   * @return true if the event was added, false otherwise
   */
  boolean addAlert(Alert a);

  void removeAlert(Alert a);

  List<DefaultAlertTreeNode> getChildren();

  int getChildCount();

  void addChild(DefaultAlertTreeNode child);

  void addChild(int index, DefaultAlertTreeNode child);

  void setChildAddOrder(List<DefaultAlertTreeNode> addOrder);

  void clearAlerts();

  /**
   * @return Returns the blink.
   */
  boolean isBlink();

  /**
   * @param blink
   *          The blink to set.
   */
  void setBlink(boolean blink);

  /**
   * @return Returns the blinkCriterion.
   */
  BlinkCriterion getBlinkCriterion();

  /**
   * @param blinkCriterion
   *          The blinkCriterion to set.
   */
  void setBlinkCriterion(BlinkCriterion blinkCriterion);

  /**
   * @return Returns the description.
   */
  String getDescription();

  /**
   * @param description
   *          The description to set.
   */
  void setDescription(String description);

  /**
   * @return Returns the eventCriterion.
   */
  AlertCriterion getAlertCriterion();

  /**
   * @param eventCriterion
   *          The eventCriterion to set.
   */
  void setAlertCriterion(AlertCriterion eventCriterion);

  /**
   * @return Returns the icon.
   */
  String getIcon();

  /**
   * @param icon
   *          The icon to set.
   */
  void setIcon(String icon);

  /**
   * @return Returns the maxSeverity.
   */
  int getMaxSeverity();

  /**
   * @param maxSeverity
   *          The maxSeverity to set.
   */
  void setMaxSeverity(int maxSeverity);

  /**
   * @return Returns the parent.
   */
  AlertTreeNode getParent();

  /**
   * @param parent
   *          The parent to set.
   */
  void setParent(AlertTreeNode parent);

  /**
   * @return Returns the text.
   */
  String getText();

  /**
   * @param text
   *          The text to set.
   */
  void setText(String text);

  /**
   * @return Returns the noMatchNode.
   */
  AlertTreeNode getNoMatchNode();

  /**
   * @param noMatchNode
   *          The noMatchNode to set.
   */
  void setNoMatchNode(DefaultAlertTreeNode noMatchNode);

  /**
   * @return the exclusive
   */
  boolean isExclusive();

  /**
   * @param exclusive
   *          the exclusive to set
   */
  void setExclusive(boolean exclusive);

}