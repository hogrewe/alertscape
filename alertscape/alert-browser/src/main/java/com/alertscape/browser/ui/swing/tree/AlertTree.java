/*
 * Created on Mar 16, 2006
 */
package com.alertscape.browser.ui.swing.tree;

import java.awt.Dimension;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * @version $Version: $
 */
public class AlertTree extends JTree
{
    private static final long serialVersionUID = 1L;
    
    public AlertTree(TreeModel model)
    {
        super(model);
        setCellRenderer(new AlertTreeNodeRenderer());
        setMinimumSize(new Dimension(200,200));
    }

    public void addEvent(Alert e)
    {
        AlertTreeModel model = (AlertTreeModel) getModel();
        model.addAlert(e);
    }
    
    public void removeEvent(Alert e)
    {
        AlertTreeModel model = (AlertTreeModel) getModel();
        model.removeAlert(e);
    }
    
    public void clearEvents()
    {
        AlertTreeModel model = (AlertTreeModel) getModel();
        model.clearAlerts();
    }
}
