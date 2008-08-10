/*
 * Created on Mar 23, 2006
 */
package com.alertscape.cev.model.panel;

import com.alertscape.common.model.AlertCollection;

/**
 * @author josh
 * @version $Version: $
 */
public abstract class AbstractEventCollectionPanelModel implements
        EventCollectionPanelModel
{
    private AlertCollection collection;

    public void setCollection(AlertCollection collection)
    {
        this.collection = collection;
    }

    public AlertCollection getCollection( )
    {
        return collection;
    }
}
