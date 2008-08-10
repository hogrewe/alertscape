/*
 * Created on Mar 16, 2006
 */
package com.alertscape.cev.ui.swing.tree;

import javax.swing.JTree;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * @version $Version: $
 */
public class CevTree extends JTree
{
    private static final long serialVersionUID = 1L;
    
    public CevTree(CevTreeModel model)
    {
        super(model);
        setCellRenderer(new CevTreeNodeRenderer());
    }

    public void addEvent(Alert e)
    {
        CevTreeModel model = (CevTreeModel) getModel();
        model.addEvent(e);
    }
    
    public void removeEvent(Alert e)
    {
        CevTreeModel model = (CevTreeModel) getModel();
        model.removeEvent(e);
    }
    
    public void clearEvents()
    {
        CevTreeModel model = (CevTreeModel) getModel();
        model.clearEvents();
    }
}
