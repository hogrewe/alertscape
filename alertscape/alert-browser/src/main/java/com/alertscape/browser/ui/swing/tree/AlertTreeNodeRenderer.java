/*
 * Created on Mar 21, 2006
 */
package com.alertscape.browser.ui.swing.tree;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.alertscape.browser.model.tree.AlertTreeNode;
import com.alertscape.browser.model.tree.DefaultAlertTreeNode;
import com.alertscape.common.model.severity.Severity;
import com.alertscape.common.model.severity.SeverityFactory;
import com.alertscape.util.ImageFinder;

/**
 * @author josh
 * @version $Version: $
 */
public class AlertTreeNodeRenderer extends DefaultTreeCellRenderer
{
    private static final long serialVersionUID = 1L;

    protected Icon[] severityIcons;

    public AlertTreeNodeRenderer( )
    {
        SeverityFactory factory = SeverityFactory.getInstance( );
        ImageFinder finder = ImageFinder.getInstance( );
        severityIcons = new Icon[factory.getNumSeverities( )];
        for (int i = 0; i < factory.getNumSeverities( ); i++) {
            Severity sev = factory.getSeverity(i);
            severityIcons[i] = finder.findImage(sev.getSmallIcon( ));
        }
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean selected, boolean expanded, boolean leaf, int row,
            boolean hasFocus)
    {
        JLabel l = (JLabel) super.getTreeCellRendererComponent(tree, value,
                selected, expanded, leaf, row, hasFocus);

        AlertTreeNode node;
        if(value instanceof DefaultAlertTreeNode) {
          node = (AlertTreeNode) value;
        } else {
          DefaultMutableTreeNode mtn = (DefaultMutableTreeNode) value;
          node = (AlertTreeNode) mtn.getUserObject();
        }
        l.setText(node.getText( ));
        Icon icon = determineIcon(node);
        if (icon != null) {
            l.setIcon(icon);
        }
        return l;
    }

    protected Icon determineIcon(AlertTreeNode node)
    {
        Icon icon = null;

        if (node.getMaxSeverity( ) >= 0) {
            icon = severityIcons[node.getMaxSeverity( )];
        }

        if (icon == null) {
            icon = ImageFinder.getInstance( ).findImage(node.getIcon( ));
        }

        return icon;
    }
}
