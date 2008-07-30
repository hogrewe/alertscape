/*
 * Created on Oct 22, 2006
 */
package com.alertscape.cev.ui.swing.tree;

import javax.swing.JFrame;

import com.alertscape.common.junit.AlertScapeTestCase;

/**
 * @author josh
 * @version $Version: $
 */
public class CevTreeTest extends AlertScapeTestCase
{
  public CevTreeTest()
  {
    super("CevTreeTest");
  }
  
  public void testShowTree()
  {
    JFrame f = new JFrame();
    CevTree tree = new CevTree(null);
  }
}
