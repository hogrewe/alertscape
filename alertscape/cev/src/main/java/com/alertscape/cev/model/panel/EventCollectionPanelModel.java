/*
 * Created on Apr 1, 2006
 */
package com.alertscape.cev.model.panel;

import com.alertscape.common.model.EventCollection;

/**
 * @author josh
 * @version $Version: $
 */
public interface EventCollectionPanelModel
{
    public void setCollection(EventCollection collection);

    public EventCollection getCollection( );
}
