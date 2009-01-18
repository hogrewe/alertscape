/*
 * Created on Mar 15, 2006
 */
package com.alertscape.browser.model.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alertscape.browser.model.criterion.AlertCriterion;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.severity.SeverityFactory;

/**
 * @author josh
 * @version $Version: $
 */
public class DefaultAlertTreeNode implements AlertTreeNode {
  private byte[] alertLock = new byte[0];

  private AlertTreeNode parent;
  private List<DefaultAlertTreeNode> children;
  /** This is the order that we should try to add the alerts to the children */
  private List<DefaultAlertTreeNode> childAddOrder;
  private AlertTreeNode noMatchNode;
  private Set<Alert> alerts;
  /** Trade off space for speed in handling severity changes */
  private Map<String, Alert>[] severityAlerts;
  private AlertCriterion alertCriterion;
  private BlinkCriterion blinkCriterion;
  private int maxSeverity;
  private String icon;
  private String text;
  private String description;
  private boolean blink;
  private boolean exclusive;

  @SuppressWarnings("unchecked")
  public DefaultAlertTreeNode() {
    alerts = new HashSet<Alert>();
    int numsevs = SeverityFactory.getInstance().getNumSeverities();
    severityAlerts = new Map[numsevs];
    for (int i = 0; i < numsevs; i++) {
      severityAlerts[i] = new HashMap<String, Alert>();
    }
    children = new ArrayList<DefaultAlertTreeNode>();
  }
  
  public DefaultAlertTreeNode(String text) {
    this();
    setText(text);
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#addAlert(com.alertscape.common.model.Alert)
   */
  public boolean addAlert(Alert a) {
    boolean added = false;

    synchronized (alertLock) {
      added = alertCriterion == null || alertCriterion.matches(a);
      alerts.add(a);
      if (added) {
        if (!addToChildren(a) && noMatchNode != null) {
          noMatchNode.addAlert(a);
        }
      }
    }

    return added;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#removeAlert(com.alertscape.common.model.Alert)
   */
  public void removeAlert(Alert a) {
    synchronized (alertLock) {
      if(alerts.remove(a)) {
        for (AlertTreeNode child : getChildAddOrder()) {
          child.removeAlert(a);
        }
      }
    }
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#getChildren()
   */
  public List<DefaultAlertTreeNode> getChildren() {
    synchronized (alertLock) {
      return Collections.unmodifiableList(children);
    }
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#getChildCount()
   */
  public int getChildCount() {
    synchronized (alertLock) {
      return children.size();
    }
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#addChild(com.alertscape.browser.model.tree.DefaultAlertTreeNode)
   */
  public void addChild(DefaultAlertTreeNode child) {
    children.add(child);
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#addChild(int, com.alertscape.browser.model.tree.DefaultAlertTreeNode)
   */
  public void addChild(int index, DefaultAlertTreeNode child) {
    children.add(index, child);
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#setChildAddOrder(java.util.List)
   */
  public void setChildAddOrder(List<DefaultAlertTreeNode> addOrder) {
    this.childAddOrder = addOrder;
  }

  protected List<DefaultAlertTreeNode> getChildAddOrder() {
    if (childAddOrder == null) {
      return children;
    } else {
      return childAddOrder;
    }
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#clearAlerts()
   */
  public void clearAlerts() {
    synchronized (alertLock) {
      alerts.clear();
      for (Map<String, Alert> element : severityAlerts) {
        element.clear();
      }
      maxSeverity = 0;
    }

  }

  protected boolean addToChildren(Alert a) {
    boolean added = false;

    for (AlertTreeNode child : getChildAddOrder()) {
      boolean childAdded = child.addAlert(a);
      if (childAdded && child.isExclusive()) {
        return childAdded;
      }
      added |= childAdded;
    }

    return added;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#isBlink()
   */
  public boolean isBlink() {
    return blink;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#setBlink(boolean)
   */
  public void setBlink(boolean blink) {
    this.blink = blink;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#getBlinkCriterion()
   */
  public BlinkCriterion getBlinkCriterion() {
    return blinkCriterion;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#setBlinkCriterion(com.alertscape.browser.model.tree.BlinkCriterion)
   */
  public void setBlinkCriterion(BlinkCriterion blinkCriterion) {
    this.blinkCriterion = blinkCriterion;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#getDescription()
   */
  public String getDescription() {
    return description;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#setDescription(java.lang.String)
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#getAlertCriterion()
   */
  public AlertCriterion getAlertCriterion() {
    return alertCriterion;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#setAlertCriterion(com.alertscape.browser.model.criterion.AlertCriterion)
   */
  public void setAlertCriterion(AlertCriterion eventCriterion) {
    this.alertCriterion = eventCriterion;
  }

  /**
   * @return Returns the events.
   */
  protected Set<Alert> getAlerts() {
    return alerts;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#getIcon()
   */
  public String getIcon() {
    return icon;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#setIcon(java.lang.String)
   */
  public void setIcon(String icon) {
    this.icon = icon;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#getMaxSeverity()
   */
  public int getMaxSeverity() {
    return maxSeverity;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#setMaxSeverity(int)
   */
  public void setMaxSeverity(int maxSeverity) {
    this.maxSeverity = maxSeverity;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#getParent()
   */
  public AlertTreeNode getParent() {
    return parent;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#setParent(com.alertscape.browser.model.tree.AlertTreeNode)
   */
  public void setParent(AlertTreeNode parent) {
    this.parent = parent;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#getText()
   */
  public String getText() {
    return text;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#setText(java.lang.String)
   */
  public void setText(String text) {
    this.text = text;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#getNoMatchNode()
   */
  public AlertTreeNode getNoMatchNode() {
    return noMatchNode;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#setNoMatchNode(com.alertscape.browser.model.tree.DefaultAlertTreeNode)
   */
  public void setNoMatchNode(DefaultAlertTreeNode noMatchNode) {
    this.noMatchNode = noMatchNode;
    if (!children.contains(noMatchNode)) {
      children.add(noMatchNode);
    }
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#isExclusive()
   */
  public boolean isExclusive() {
    return exclusive;
  }

  /* (non-Javadoc)
   * @see com.alertscape.browser.model.tree.AlertTreeNode#setExclusive(boolean)
   */
  public void setExclusive(boolean exclusive) {
    this.exclusive = exclusive;
  }
}
