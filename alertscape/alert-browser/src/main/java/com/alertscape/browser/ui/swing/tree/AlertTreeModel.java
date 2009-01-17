/*
 * Created on Mar 16, 2006
 */
package com.alertscape.browser.ui.swing.tree;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.alertscape.browser.model.tree.AlertTreeNode;
import com.alertscape.browser.model.tree.DefaultAlertTreeNode;
import com.alertscape.common.model.Alert;

/**
 * @author josh
 * @version $Version: $
 */
public class AlertTreeModel implements TreeModel {
  private static final long serialVersionUID = -2829087511737718103L;
  private AlertTreeNode root;

  public AlertTreeModel(AlertTreeNode root) {
//    super(new DefaultMutableTreeNode(root));
    this.root = root;
  }

  public void addAlert(Alert e) {
    root.addAlert(e);
  }

  public void removeAlert(Alert e) {
    root.removeAlert(e);
  }

  public void clearAlerts() {
    root.clearAlerts();
  }

  public Object getChild(Object parent, int index) {
    AlertTreeNode child = null;
    if (parent instanceof DefaultAlertTreeNode) {
      AlertTreeNode n = (AlertTreeNode) parent;
      child = n.getChildren().get(index);
    }
    return child;
  }

  public int getChildCount(Object parent) {
    int count = 0;
    if (parent instanceof DefaultAlertTreeNode) {
      count = ((AlertTreeNode) parent).getChildCount();
    }
    return count;
  }

  public boolean isLeaf(Object node) {
    boolean leaf = true;

    if (node instanceof DefaultAlertTreeNode) {
      AlertTreeNode ctn = (AlertTreeNode) node;
      leaf = ctn.getChildCount() < 1;
    }

    return leaf;
  }

  public int getIndexOfChild(Object parent, Object child) {
    int index = -1;

    if (parent instanceof DefaultAlertTreeNode) {
      AlertTreeNode ctn = (AlertTreeNode) parent;
      index = ctn.getChildren().indexOf(child);
    }

    return index;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
   */
  public void addTreeModelListener(TreeModelListener l) {
    // TODO Auto-generated method stub

  }

  public Object getRoot() {
    return root;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
   */
  public void removeTreeModelListener(TreeModelListener l) {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
   */
  public void valueForPathChanged(TreePath path, Object newValue) {
    // TODO Auto-generated method stub

  }
}
