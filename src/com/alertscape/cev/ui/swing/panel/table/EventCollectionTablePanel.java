/*
 * Created on Apr 1, 2006
 */
package com.alertscape.cev.ui.swing.panel.table;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import com.alertscape.cev.model.EventCollection;
import com.alertscape.cev.model.severity.Severity;
import com.alertscape.cev.ui.swing.panel.EventCollectionPanel;
import com.alertscape.cev.ui.swing.panel.table.renderer.DefaultEventCellRenderer;
import com.alertscape.cev.ui.swing.panel.table.renderer.SeverityEventCellRenderer;

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
        EventColumn c;
        c = new EventColumn("Event ID", "eventId");
        columns.add(c);
        c = new EventColumn("Description", "longDescription");
        columns.add(c);
        c = new EventColumn("Type", "type");
        columns.add(c);
        c = new EventColumn("Severity", "severity");
        columns.add(c);
        c = new EventColumn("Count", "count");
        columns.add(c);
        c = new EventColumn("Source", "sourceId");
        columns.add(c);
        c = new EventColumn("Item", "item");
        c.setWidth(200);
        columns.add(c);
        c = new EventColumn("Manager", "itemManager");
        c.setWidth(200);
        columns.add(c);
        c = new EventColumn("Item Type", "itemType");
        c.setWidth(200);
        columns.add(c);
        c = new EventColumn("Manager Type", "itemManagerType");
        c.setWidth(200);
        columns.add(c);
        c = new EventColumn("First", "firstOccurence");
        c.setWidth(250);
        columns.add(c);
        c = new EventColumn("Last", "lastOccurence");
        c.setWidth(250);
        columns.add(c);

        model = new EventCollectionTableModel(columns);
        EventCollectionTableColumnModel columnModel = new EventCollectionTableColumnModel(
                columns);
        // collectionTable = new JTable(model, columnModel);
        // collectionTable = new JTable(model);
        collectionTable = new EventTable(model, columnModel);
        sorter = new TableRowSorter<EventCollectionTableModel>(model);

        TableCellRenderer defaultRenderer = new DefaultEventCellRenderer( );
        TableCellRenderer sevRenderer = new SeverityEventCellRenderer( );

        collectionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        collectionTable.setRowSorter(sorter);
        collectionTable.setDefaultRenderer(Long.class, defaultRenderer);
        collectionTable.setDefaultRenderer(Object.class, defaultRenderer);
        collectionTable.setDefaultRenderer(Date.class, defaultRenderer);
        collectionTable.setDefaultRenderer(Severity.class, sevRenderer);
        collectionTable
                .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // collectionTable.setRowSelectionAllowed(true);
        setLayout(new BorderLayout( ));
        JScrollPane tableScroller = new JScrollPane(collectionTable);
        add(tableScroller, BorderLayout.CENTER);
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
