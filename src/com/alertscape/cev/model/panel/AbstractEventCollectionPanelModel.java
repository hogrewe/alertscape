/*
 * Created on Mar 23, 2006
 */
package com.alertscape.cev.model.panel;

import com.alertscape.cev.model.EventChangeListener;
import com.alertscape.cev.model.EventCollection;

/**
 * @author josh
 * @version $Version: $
 */
public abstract class AbstractEventCollectionPanelModel implements
        EventCollectionPanelModel, EventChangeListener
{
    private EventCollection collection;

    public void setCollection(EventCollection collection)
    {
        if (this.collection == null) {
            this.collection.removeEventChangeListener(this);
        }
        this.collection = collection;
        collection.addEventChangeListener(this);
    }

    public EventCollection getCollection( )
    {
        return collection;
    }
}
