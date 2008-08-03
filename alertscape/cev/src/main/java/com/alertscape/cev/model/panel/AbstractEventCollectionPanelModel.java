/*
 * Created on Mar 23, 2006
 */
package com.alertscape.cev.model.panel;

import com.alertscape.common.model.EventCollection;

/**
 * @author josh
 * @version $Version: $
 */
public abstract class AbstractEventCollectionPanelModel implements
        EventCollectionPanelModel
{
    private EventCollection collection;

    public void setCollection(EventCollection collection)
    {
        this.collection = collection;
    }

    public EventCollection getCollection( )
    {
        return collection;
    }
}
