/*
 * Created on Apr 2, 2006
 */
package com.alertscape.util;

/**
 * @author josh
 * @version $Version: $
 */
public class PrimitiveHelper
{
    /**
     * This class will take a primtive type and return the type of class that
     * can contain it (e.g. given int.class, return Integer.class)
     * 
     * @param c The primitive type
     * @return
     */
    public static Class<?> getContainingClass(Class<?> c)
    {
        if(c == long.class)
        {
            return Long.class;
        }
        else if(c == int.class)
        {
            return Integer.class;
        }
        else if(c == float.class)
        {
            return Float.class;
        }
        else if(c == double.class)
        {
            return Double.class;
        }
        else if(c == byte.class)
        {
            return Byte.class;
        }
        else if(c == char.class)
        {
            return Character.class;
        }
        else if(c == short.class)
        {
            return Short.class;
        }
        else if(c == boolean.class)
        {
            return Boolean.class;
        }
        else
        {
            return Void.class;
        }
    }
}
