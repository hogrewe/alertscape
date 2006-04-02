/*
 * Created on Apr 1, 2006
 */
package com.alertscape.cev.ui.swing.panel.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;

import com.alertscape.cev.model.EventCollection;
import com.alertscape.cev.ui.swing.panel.EventCollectionPanel;

/**
 * @author josh
 * @version $Version: $
 */
public class EventCollectionTablePanel extends JPanel implements
        EventCollectionPanel
{
    private static final long serialVersionUID = 1L;
    
    private EventCollectionTableModel model;
    private JTable collectionTable;
    
    public EventCollectionTablePanel()
    {
        init();
    }
    
    public void init()
    {
        List<EventColumn> columns = new ArrayList<EventColumn>();
        
        model = new EventCollectionTableModel(columns, "eventId", false);
        collectionTable = new JTable(model);
    }

    public void setCollection(EventCollection collection)
    {
    }

    public EventCollection getCollection( )
    {
        // TODO Auto-generated method stub
        return null;
    }
}
