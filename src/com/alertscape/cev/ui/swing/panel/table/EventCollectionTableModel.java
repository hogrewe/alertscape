/*
 * Created on Apr 1, 2006
 */
package com.alertscape.cev.ui.swing.panel.table;

import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.alertscape.cev.model.Event;
import com.alertscape.cev.model.EventChange;
import com.alertscape.cev.model.EventChangeListener;
import com.alertscape.cev.model.EventCollection;
import com.alertscape.cev.model.panel.EventCollectionPanelModel;

/**
 * @author josh
 * @version $Version: $
 */
public class EventCollectionTableModel extends AbstractTableModel implements
        EventCollectionPanelModel, EventChangeListener
{
    // private static Logger logger = Logger
    // .getLogger(EventCollectionTableModel.class);

    private static final long serialVersionUID = 1L;
    private List<EventColumn> columns;

    private EventCollection collection;

    public EventCollectionTableModel(List<EventColumn> columns)
    {
        setColumns(columns);
    }
    
    public void setCollection(EventCollection collection)
    {
        this.collection = collection;
        this.collection.addEventChangeListener(this);
    }

    public EventCollection getCollection( )
    {
        return collection;
    }

    public void handleChange(EventChange change)
    {
        fireTableDataChanged();
    }

    public int getRowCount( )
    {
        return getCollection( ).getEventCount( );
    }

    public int getColumnCount( )
    {
        return columns.size( );
    }

    public String getColumnName(int columnIndex)
    {
        return columns.get(columnIndex).getDisplayName( );
    }

    public Class<?> getColumnClass(int columnIndex)
    {
        Class c = Object.class;
        EventColumn column = columns.get(columnIndex);
        if (column.getPropertyGetter( ) != null)
        {
            c = column.getColumnClass();
        }
        return c;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        EventColumn column = columns.get(columnIndex);
        Event e = collection.getEventAt(rowIndex);
        return column.getValue(e);
    }
    
    public Event getEventAt(int rowIndex)
    {
        return collection.getEventAt(rowIndex);
    }

    public void setColumns(List<EventColumn> columns)
    {
        this.columns = columns;
    }

    public List<EventColumn> getColumns( )
    {
        return Collections.unmodifiableList(columns);
    }
}
