/*
 * Created on Mar 21, 2006
 */
package com.alertscape.util;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * @author josh
 * @version $Version: $
 */
public class ImageFinder
{
    private static ImageFinder inst = new ImageFinder( );

    private Map<String, Icon> images = new HashMap<String, Icon>( );

    protected ImageFinder( )
    {

    }

    public static ImageFinder getInstance( )
    {
        return inst;
    }

    public Icon findImage(String path)
    {
        if(path == null)
        {
            return null;
        }
        
        Icon icon = images.get(path);

        if (icon == null) {
            icon = new ImageIcon(getClass( ).getResource(path));
            if (icon != null) {
                images.put(path, icon);
            }
        }

        return icon;
    }
}
