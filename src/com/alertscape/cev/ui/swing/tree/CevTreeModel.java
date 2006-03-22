/*
 * Created on Mar 16, 2006
 */
package com.alertscape.cev.ui.swing.tree;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.alertscape.cev.model.tree.CevTreeNode;

/**
 * @author josh
 * @version $Version: $
 */
public class CevTreeModel implements TreeModel
{
    private CevTreeNode root;

    public CevTreeModel()
    {
        
    }
    
    public CevTreeModel(CevTreeNode root)
    {
        setRoot(root);
    }
    
    public Object getRoot( )
    {
        return root;
    }

    public Object getChild(Object parent, int index)
    {
        CevTreeNode child = null;
        if(parent instanceof CevTreeNode)
        {
            CevTreeNode n = (CevTreeNode) parent;
        }
        return child;
    }

    public int getChildCount(Object parent)
    {
        int count = 0;
        if(parent instanceof CevTreeNode)
        {
            
        }
        return 0;
    }

    public boolean isLeaf(Object node)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void valueForPathChanged(TreePath path, Object newValue)
    {
        // TODO Auto-generated method stub

    }

    public int getIndexOfChild(Object parent, Object child)
    {
        // TODO Auto-generated method stub
        return 0;
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
