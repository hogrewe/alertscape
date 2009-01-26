/*
 * Created on Mar 21, 2006
 */
package com.alertscape.browser.ui.swing.tree;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.alertscape.browser.model.tree.AlertTreeNode;
import com.alertscape.browser.model.tree.DefaultAlertTreeNode;
import com.alertscape.common.model.severity.Severity;
import com.alertscape.util.ImageFinder;

/**
 * @author josh
 * @version $Version: $
 */
public class AlertTreeNodeRenderer extends DefaultTreeCellRenderer {
  private static final long serialVersionUID = 1L;
  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
      boolean leaf, int row, boolean hasFocus) {
    JLabel l = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

    AlertTreeNode node;
    if (value instanceof DefaultAlertTreeNode) {
      node = (AlertTreeNode) value;
    } else {
      DefaultMutableTreeNode mtn = (DefaultMutableTreeNode) value;
      node = (AlertTreeNode) mtn.getUserObject();
    }
    l.setText(node.getDisplayText());
    Icon icon = determineIcon(node, l.getIcon());
    if (icon != null) {
      l.setIcon(icon);
    }

    if (selected) {
      l.setBackground(getBackgroundSelectionColor());
    } else {
      if (node.getAlertCount() == 0) {
        l.setForeground(Color.gray);
        l.setBackground(getBackgroundNonSelectionColor());
      } else {
        // l.setForeground(node.getMaxSeverity().getForegroundColor());
        // l.setBackground(node.getMaxSeverity().getBackgroundColor());
        // l.setOpaque(true);
      }
    }
    return l;
  }

  protected Icon determineIcon(AlertTreeNode node, Icon defaultIcon) {
    Severity severity = node.getMaxSeverity();
    
    ImageFinder finder = ImageFinder.getInstance();
    Icon nodeIcon = finder.findImage(node.getIcon());
    if(nodeIcon == null) {
      nodeIcon = defaultIcon;
    }
    if(severity.getSmallIcon() != null) {
      Icon[] icons = new Icon[2];
      icons[0] = nodeIcon;
      icons[1] = finder.findImage(severity.getSmallIcon());
      nodeIcon = new CompositeIcon(icons);
    }

    return nodeIcon;
  }
}
