/*
 * Created on Apr 1, 2006
 */
package com.alertscape.cev.ui.swing.panel.table;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

import com.alertscape.cev.model.EventCollection;
import com.alertscape.cev.ui.swing.panel.EventCollectionPanel;
import com.alertscape.cev.ui.swing.panel.table.renderer.DefaultEventCellRenderer;

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
    private TableRowSorter<EventCollectionTableModel> sorter;

    public EventCollectionTablePanel( )
    {
        init( );
    }

    public void init( )
    {
        List<EventColumn> columns = new ArrayList<EventColumn>( );
        columns.add(new EventColumn("Event ID", "eventId"));
        columns.add(new EventColumn("Description", "longDescription"));
        columns.add(new EventColumn("Type", "type"));
        columns.add(new EventColumn("Severity", "severity"));
        columns.add(new EventColumn("Count", "count"));
        columns.add(new EventColumn("Source", "source"));
        columns.add(new EventColumn("Item", "item"));
        columns.add(new EventColumn("Manager", "itemManager"));
        columns.add(new EventColumn("Item Type", "itemType"));
        columns.add(new EventColumn("Manager Type", "itemManagerType"));
        columns.add(new EventColumn("First", "firstOccurence"));
        columns.add(new EventColumn("Last", "lastOccurence"));

        model = new EventCollectionTableModel(columns);
        collectionTable = new JTable(model);
        sorter = new TableRowSorter<EventCollectionTableModel>(model);
        collectionTable.setRowSorter(sorter);
        collectionTable.setDefaultRenderer(long.class,
                new DefaultEventCellRenderer( ));
        setLayout(new BorderLayout( ));
        add(new JScrollPane(collectionTable), BorderLayout.CENTER);
    }

    public void setCollection(EventCollection collection)
    {
        model.setCollection(collection);
    }

    public EventCollection getCollection( )
    {
        return model.getCollection( );
    }
}
