/*
 * Created on Apr 1, 2006
 */
package com.alertscape.cev.ui.swing.panel.collection;

import com.alertscape.cev.model.EventCollection;

/**
 * @author josh
 * @version $Version: $
 */
public interface EventCollectionPanel
{
    public void setCollection(EventCollection collection);
    public EventCollection getCollection();
}
