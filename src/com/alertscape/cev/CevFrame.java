/*
 * Created on Mar 21, 2006
 */
package com.alertscape.cev;

import javax.swing.JFrame;

import com.alertscape.cev.model.tree.CevTreeNode;
import com.alertscape.cev.ui.swing.tree.CevTree;
import com.alertscape.cev.ui.swing.tree.CevTreeModel;

/**
 * @author josh
 * @version $Version: $
 */
public class CevFrame extends JFrame
{
    private static final long serialVersionUID = 1L;
    
    private CevTree tree;

    public CevFrame()
    {
        init();
        setVisible(true);
    }
    
    public void init()
    {
        setSize(800,600);
        CevTreeNode root = new CevTreeNode();
        root.setText("Root yo");
        CevTreeModel model = new CevTreeModel(root);
        tree = new CevTree(model);
        add(tree);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
