/*
 * Created on Mar 15, 2006
 */
package com.alertscape.cev.ui.swing;

import javax.swing.JFrame;

/**
 * @author josh
 * @version $Version: $
 */
public class Cev extends JFrame
{
    private static final long serialVersionUID = 1L;

    /**
     * This is the default constructor
     */
    public Cev( )
    {
        super( );
        initialize( );
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize( )
    {
        this.setSize(600, 400);
    }
}
