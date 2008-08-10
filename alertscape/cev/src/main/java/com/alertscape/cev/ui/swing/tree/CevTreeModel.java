/*
 * Created on Mar 16, 2006
 */
package com.alertscape.cev.ui.swing.tree;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.alertscape.cev.model.tree.CevTreeNode;
import com.alertscape.common.model.Alert;

/**
 * @author josh
 * @version $Version: $
 */
public class CevTreeModel implements TreeModel
{
    private CevTreeNode root;

    public CevTreeModel( )
    {

    }

    public CevTreeModel(CevTreeNode root)
    {
        setRoot(root);
    }
    
    public void addEvent(Alert e)
    {
        root.addEvent(e);
    }
    
    public void removeEvent(Alert e)
    {
        root.removeEvent(e);
    }
    
    public void clearEvents()
    {
        root.clearEvents();
    }

    public Object getRoot( )
    {
        return root;
    }

    public Object getChild(Object parent, int index)
    {
        CevTreeNode child = null;
        if (parent instanceof CevTreeNode) {
            CevTreeNode n = (CevTreeNode) parent;
            child = n.getChildren().get(index);
        }
        return child;
    }

    public int getChildCount(Object parent)
    {
        int count = 0;
        if (parent instanceof CevTreeNode) {
            count = ((CevTreeNode) parent).getChildCount( );
        }
        return count;
    }

    public boolean isLeaf(Object node)
    {
        boolean leaf = true;

        if (node instanceof CevTreeNode) {
            CevTreeNode ctn = (CevTreeNode) node;
            leaf = ctn.getChildCount( ) < 1;
        }

        return leaf;
    }

    public void valueForPathChanged(TreePath path, Object newValue)
    {
        // TODO Auto-generated method stub

    }

    public int getIndexOfChild(Object parent, Object child)
    {
        int index = -1;
        
        if (parent instanceof CevTreeNode) {
            CevTreeNode ctn = (CevTreeNode) parent;
            index = ctn.getChildren().indexOf(child);
        }
        
        return index;
    }

    public void addTreeModelListener(TreeModelListener l)
    {
        // TODO Auto-generated method stub

    }

    public void removeTreeModelListener(TreeModelListener l)
    {
        // TODO Auto-generated method stub

    }

    public void setRoot(CevTreeNode root)
    {
        this.root = root;
    }
}
