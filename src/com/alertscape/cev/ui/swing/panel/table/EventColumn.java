/*
 * Created on Apr 1, 2006
 */
package com.alertscape.cev.ui.swing.panel.table;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.alertscape.cev.model.Event;
import com.alertscape.util.GetterHelper;
import com.alertscape.util.PrimitiveHelper;

class EventColumn
{
    private static Logger logger = Logger.getLogger(EventColumn.class);

    String displayName;
    String propertyName;
    private Method propertyGetter;
    private Class columnClass;

    public EventColumn(String displayName, String propertyName)
    {
        setDisplayName(displayName);
        setPropertyName(propertyName);
    }

    public Class getColumnClass( )
    {
        return columnClass;
    }

    public String getDisplayName( )
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getPropertyName( )
    {
        return propertyName;
    }

    public void setPropertyName(String propertyName)
    {
        if (this.propertyName == null
                || !this.propertyName.equals(propertyName))
        {
            propertyGetter = GetterHelper.makeEventGetter(propertyName);
            if (propertyGetter != null)
            {
                columnClass = propertyGetter.getReturnType( );
                if (columnClass.isPrimitive( ))
                {
                    columnClass = PrimitiveHelper
                            .getContainingClass(columnClass);
                }
            }
        }
        this.propertyName = propertyName;
    }

    public Object getValue(Event e)
    {
        Object o = null;
        if (propertyGetter != null)
        {
            try
            {
                o = propertyGetter.invoke(e, new Object[0]);
            } catch (Exception e1)
            {
                logger.error("Couldn't get value for column "
                        + getDisplayName( ), e1);
            }
        }
        return o;
    }

    public Method getPropertyGetter( )
    {
        return propertyGetter;
    }
}