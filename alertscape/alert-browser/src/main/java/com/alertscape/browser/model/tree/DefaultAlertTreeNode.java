/*
 * Created on Mar 15, 2006
 */
package com.alertscape.browser.model.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.alertscape.browser.model.criterion.AlertCriterion;
import com.alertscape.browser.ui.swing.panel.collection.tree.TreeMatcherEditor;
import com.alertscape.browser.ui.swing.tree.AlertTreeModel;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.severity.Severity;
import com.alertscape.common.model.severity.SeverityFactory;

/**
 * @author josh
 * @version $Version: $
 */
public class DefaultAlertTreeNode implements AlertTreeNode {
  private static final ASLogger LOG = ASLogger.getLogger(DefaultAlertTreeNode.class);
  /** The maximum amount of time a removable node is allowed to hang around before being reaped */
  private static final long MAX_EMPTY_TIME = 5 * 60 * 1000;
  private byte[] alertLock = new byte[0];

  private AlertTreeNode parent;
  private List<AlertTreeNode> children;
  /** This is the order that we should try to add the alerts to the children */
  private List<AlertTreeNode> childAddOrder;
  private AlertTreeNode noMatchNode;
  private Map<Alert, Alert> alerts;
  private Map<Severity, Integer> severityCounts;
  private AlertCriterion alertCriterion;
  private BlinkCriterion blinkCriterion;
  private Severity maxSeverity;
  private String icon;
  private String text;
  private String description;
  private boolean blink;
  private boolean exclusive;
  private AlertMatcher matcher;
  private TreeMatcherEditor matcherEditor;
  private AlertTreeModel treeModel;
  private boolean removable;
  private long emptyTime;

  public DefaultAlertTreeNode() {
    alerts = new HashMap<Alert, Alert>();
    int numsevs = SeverityFactory.getInstance().getNumSeverities();
    severityCounts = new HashMap<Severity, Integer>(numsevs);
    for (int i = 0; i < numsevs; i++) {
      Severity s = SeverityFactory.getInstance().getSeverity(i);
      severityCounts.put(s, 0);
    }
    maxSeverity = SeverityFactory.getInstance().getSeverity(0);
    children = new ArrayList<AlertTreeNode>();
    matcherEditor = new TreeMatcherEditor();
  }

  public DefaultAlertTreeNode(String text) {
    this();
    setText(text);
  }

  public boolean addAlert(Alert a) {
    boolean added = false;

    synchronized (alertLock) {
      added = matcher == null || matcher.matches(a);
      if (added) {
        Alert existing = alerts.get(a);
        Severity startingSev = getMaxSeverity();
        int startingCount = alerts.size();
        if (existing == null || existing.getSeverity() != a.getSeverity()) {
          int count;
          if (existing != null) {
            count = severityCounts.get(existing.getSeverity());
            count--;
            severityCounts.put(existing.getSeverity(), count);
          }
          count = severityCounts.get(a.getSeverity());
          count++;
          severityCounts.put(a.getSeverity(), count);
        }
        determineMaxSeverity();
        alerts.put(a, a);
        if (treeModel != null && (alerts.size() != startingCount || getMaxSeverity() != startingSev)) {
          notifyChanged();
        }
        matcherEditor.addAlert(a);
        if (!addToChildren(a)) {
          addNonChildMatchingAlert(a);
        }
      }
    }

    return added;
  }

  /**
   * 
   */
  private void notifyChanged() {
    try {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          synchronized (treeModel) {
            treeModel.nodeChanged(DefaultAlertTreeNode.this);
          }
        }

      });
    } catch (Exception e) {
      LOG.error("Couldn't notify of changed tree nodes", e);
    }
  }

  /**
   * 
   */
  private void determineMaxSeverity() {
    maxSeverity = SeverityFactory.getInstance().getSeverity(0);
    for (Severity s : severityCounts.keySet()) {
      if (s.getLevel() > maxSeverity.getLevel() && severityCounts.get(s) > 0) {
        maxSeverity = s;
      }
    }
  }

  protected void addNonChildMatchingAlert(Alert alert) {
    if (noMatchNode != null) {
      noMatchNode.addAlert(alert);
    }
  }

  public void removeAlert(Alert a) {
    synchronized (alertLock) {
      Severity startingSev = getMaxSeverity();
      int startingCount = alerts.size();
      Alert existing = alerts.remove(a); // check if this alert is already in this node
      if (existing != null) {
        int count = severityCounts.get(existing.getSeverity());
        count--;
        severityCounts.put(existing.getSeverity(), count);
        determineMaxSeverity();
        matcherEditor.removeAlert(a);
        if (treeModel != null && (alerts.size() != startingCount || getMaxSeverity() != startingSev)) {
          notifyChanged();
        }
        // final ArrayList<AlertTreeNode> childrenToRemove = new ArrayList<AlertTreeNode>(1);
        for (AlertTreeNode child : getChildAddOrder()) {
          child.removeAlert(a);
        }
        if(count < 1) {
          setEmptyTime(System.currentTimeMillis());
        }
      }
    }

  }

  public List<AlertTreeNode> getChildren() {
    return children;
  }

  public int getChildCount() {
    return children.size();
  }

  public void addChild(AlertTreeNode child) {
    addChild(child, children.size());
  }

  public void setChildren(List<AlertTreeNode> children) {
    for (AlertTreeNode child : children) {
      addChild(child);
    }
  }

  public void addChild(AlertTreeNode child, final int index) {
    children.add(index, child);
    child.setTreeModel(treeModel);
    child.setParent(this);
    if (treeModel != null) {
      try {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            synchronized (treeModel) {
              treeModel.nodesWereInserted(DefaultAlertTreeNode.this, new int[] { index });
            }
          }

        });
      } catch (Exception e) {
        LOG.error("Couldn't notify of changed tree nodes", e);
      }
    }
  }

  public void insert(MutableTreeNode child, int index) {
    DefaultAlertTreeNode alertNode = (DefaultAlertTreeNode) child;
    children.add(index, alertNode);
    child.setParent(this);
  }

  public int getAlertCount() {
    return alerts.size();
  }

  public void setChildAddOrder(List<AlertTreeNode> addOrder) {
    this.childAddOrder = addOrder;
  }

  protected List<AlertTreeNode> getChildAddOrder() {
    if (childAddOrder == null) {
      return children;
    } else {
      return childAddOrder;
    }
  }

  public void clearAlerts() {
    synchronized (alertLock) {
      alerts.clear();
      matcherEditor.clearAlerts();
      for (Severity s : severityCounts.keySet()) {
        severityCounts.put(s, 0);
      }
      determineMaxSeverity();
      if (treeModel != null) {
        notifyChanged();
      }
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

  public boolean isBlink() {
    return blink;
  }

  public void setBlink(boolean blink) {
    this.blink = blink;
  }

  public BlinkCriterion getBlinkCriterion() {
    return blinkCriterion;
  }

  public void setBlinkCriterion(BlinkCriterion blinkCriterion) {
    this.blinkCriterion = blinkCriterion;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public AlertCriterion getAlertCriterion() {
    return alertCriterion;
  }

  public void setAlertCriterion(AlertCriterion eventCriterion) {
    this.alertCriterion = eventCriterion;
  }

  /**
   * @return Returns the events.
   */
  protected Collection<Alert> getAlerts() {
    return alerts.keySet();
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public Severity getMaxSeverity() {
    return maxSeverity;
  }

  public AlertTreeNode getParent() {
    return parent;
  }

  public void setParent(AlertTreeNode parent) {
    this.parent = parent;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public AlertTreeNode getNoMatchNode() {
    return noMatchNode;
  }

  public void setNoMatchNode(DefaultAlertTreeNode noMatchNode) {
    this.noMatchNode = noMatchNode;
    if (!children.contains(noMatchNode)) {
      children.add(noMatchNode);
    }
  }

  public boolean isExclusive() {
    return exclusive;
  }

  public void setExclusive(boolean exclusive) {
    this.exclusive = exclusive;
  }

  /**
   * @return the matcher
   */
  public AlertMatcher getMatcher() {
    return matcher;
  }

  /**
   * @param matcher
   *          the matcher to set
   */
  public void setMatcher(AlertMatcher matcher) {
    this.matcher = matcher;
  }

  public Enumeration<? extends AlertTreeNode> children() {
    return Collections.enumeration(children);
  }

  public boolean getAllowsChildren() {
    return true;
  }

  public TreeNode getChildAt(int childIndex) {
    return children.get(childIndex);
  }

  public int getIndex(TreeNode node) {
    return children.indexOf(node);
  }

  public boolean isLeaf() {
    return children.size() == 0;
  }

  @Override
  public String toString() {
    return getText();
  }

  public void remove(int index) {
    children.remove(index);
  }

  public void remove(MutableTreeNode node) {
    children.remove(node);
  }

  public void removeFromParent() {
    parent.remove(this);
  }

  public void setParent(MutableTreeNode newParent) {
    parent = (AlertTreeNode) newParent;
  }

  public String getDisplayText() {
    return getText() + " (" + alerts.size() + ")";
  }

  public void setUserObject(Object object) {
  }

  /**
   * @return the matcherEditor
   */
  public TreeMatcherEditor getMatcherEditor() {
    return matcherEditor;
  }

  /**
   * @return the treeModel
   */
  public AlertTreeModel getTreeModel() {
    return treeModel;
  }

  /**
   * @param treeModel
   *          the treeModel to set
   */
  public void setTreeModel(AlertTreeModel treeModel) {
    this.treeModel = treeModel;
    for (AlertTreeNode child : children) {
      child.setTreeModel(treeModel);
      child.setParent(this);
    }
  }

  // purpose of this method is to iterate the tree and scrub any child nodes that are empty
  public boolean scrubEmptyNodes() {
    synchronized (alertLock) {
      // recurse through subnodes first
      final ArrayList<AlertTreeNode> childrenToRemove = new ArrayList<AlertTreeNode>();
      for (AlertTreeNode child : getChildAddOrder()) {
        boolean isEmpty = child.scrubEmptyNodes();
        if (isEmpty) {
          // Only remove a removable node
          if (child.isRemovable() && child.getEmptyTime()  +  MAX_EMPTY_TIME < System.currentTimeMillis()) {
            // if a child needs removing, then add it to the list
            childrenToRemove.add(child);
          }
        }
      }

      // remove the children that are empty prior to returning my own node status
      // grab all of the indexes from the original list
      final int[] indexes = new int[childrenToRemove.size()];
      for (int i = 0; i < childrenToRemove.size(); i++) {
        AlertTreeNode child = childrenToRemove.get(i);
        int index = this.getIndex(child);
        indexes[i] = index;
      }

      // remove the child nodes
      for (int i = 0; i < childrenToRemove.size(); i++) {
        AlertTreeNode child = childrenToRemove.get(i);
        remove(child);
      }

      // tell the tree model that we removed some nodes
      if (treeModel != null && ! childrenToRemove.isEmpty()) {
        final DefaultAlertTreeNode me = this;
        synchronized (treeModel) {
          treeModel.nodesWereRemoved(me, indexes, childrenToRemove.toArray());
        }
      }

      return this.getAlertCount() == 0;
    }
  }

  public boolean isRemovable() {
    return removable;
  }

  public void setRemovable(boolean removable) {
    this.removable = removable;
  }

  /**
   * @return the emptyTime
   */
  public long getEmptyTime() {
    return emptyTime;
  }

  /**
   * @param emptyTime the emptyTime to set
   */
  public void setEmptyTime(long emptyTime) {
    this.emptyTime = emptyTime;
  }
}
