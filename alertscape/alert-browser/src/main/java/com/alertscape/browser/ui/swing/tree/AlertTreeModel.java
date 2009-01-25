/*
 * Created on Mar 16, 2006
 */
package com.alertscape.browser.ui.swing.tree;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import com.alertscape.browser.model.tree.AlertTreeNode;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;

/**
 * @author josh
 * @version $Version: $
 */
public class AlertTreeModel extends DefaultTreeModel {
  private static final ASLogger LOG = ASLogger.getLogger(AlertTreeModel.class);
  private static final long serialVersionUID = -2829087511737718103L;

  public AlertTreeModel(AlertTreeNode root) {
    super(root);
    root.setTreeModel(this);
  }

  public void addAlert(Alert e) {
    ((AlertTreeNode) getRoot()).addAlert(e);
  }

  public void removeAlert(Alert e) {
    ((AlertTreeNode) getRoot()).removeAlert(e);
  }

  public void clearAlerts() {
    ((AlertTreeNode) getRoot()).clearAlerts();
  }

  @Override
  public void setRoot(TreeNode root) {
    super.setRoot(root);
    ((AlertTreeNode) root).setTreeModel(this);
  }
}
