/*
 * Created on Apr 1, 2006
 */
package com.alertscape.cev.model.panel;

import com.alertscape.common.model.AlertCollection;

/**
 * @author josh
 * @version $Version: $
 */
public interface EventCollectionPanelModel
{
    public void setCollection(AlertCollection collection);

    public AlertCollection getCollection( );
}
