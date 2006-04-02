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
import com.alertscape.cev.model.SortedEventCollection;
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
    private String sortField;
    private boolean sortDescending;

    private SortedEventCollection collection;

    public EventCollectionTableModel(List<EventColumn> columns,
            String sortField, boolean desc)
    {
        setSortField(sortField);
        setSortDescending(desc);
        setColumns(columns);
    }
    
    public void setCollection(EventCollection collection)
    {
        this.collection = new SortedEventCollection(collection);
        this.collection.setSortedField(getSortField(), isSortDescending());
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
            c = column.getPropertyGetter( ).getReturnType( );
        }
        return c;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        EventColumn column = columns.get(columnIndex);
        Event e = collection.getEventAt(rowIndex);
        return column.getValue(e);
    }

    public void setColumns(List<EventColumn> columns)
    {
        this.columns = columns;
    }

    public List<EventColumn> getColumns( )
    {
        return Collections.unmodifiableList(columns);
    }

    public boolean isSortDescending( )
    {
        return sortDescending;
    }

    public void setSortDescending(boolean desc)
    {
        this.sortDescending = desc;
        if (collection != null)
        {
            collection
                    .setSortedField(getSortField( ), isSortDescending( ));
        }
    }

    public String getSortField( )
    {
        return sortField;
    }

    public void setSortField(String sortField)
    {
        this.sortField = sortField;
        if (collection != null)
        {
            collection
                    .setSortedField(getSortField( ), isSortDescending( ));
        }
    }

}
