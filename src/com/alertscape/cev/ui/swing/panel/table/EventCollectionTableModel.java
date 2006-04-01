/*
 * Created on Apr 1, 2006
 */
package com.alertscape.cev.ui.swing.panel.table;

import java.util.Collections;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.alertscape.cev.model.Event;
import com.alertscape.cev.model.EventChange;
import com.alertscape.cev.model.SortedEventCollection;
import com.alertscape.cev.model.panel.AbstractEventCollectionPanelModel;

/**
 * @author josh
 * @version $Version: $
 */
public class EventCollectionTableModel extends
        AbstractEventCollectionPanelModel implements TableModel
{
//    private static Logger logger = Logger
//            .getLogger(EventCollectionTableModel.class);

    private List<EventColumn> columns;
    private String sortField;
    private boolean sortDescending;

    private SortedEventCollection sortedCollection;

    public EventCollectionTableModel(List<EventColumn> columns,
            String sortField, boolean desc)
    {
        setSortField(sortField);
        setSortDescending(desc);
        setColumns(columns);
        sortedCollection = new SortedEventCollection( );
        sortedCollection.setSortedField(sortField, desc);
        setCollection(sortedCollection);
    }

    public void handleChange(EventChange change)
    {
        // TODO Auto-generated method stub

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

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        EventColumn column = columns.get(columnIndex);
        Event e = sortedCollection.getEventAt(rowIndex);
        return column.getValue(e);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
    }

    public void addTableModelListener(TableModelListener l)
    {
        // TODO Auto-generated method stub

    }

    public void removeTableModelListener(TableModelListener l)
    {
        // TODO Auto-generated method stub

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
        if(sortedCollection != null)
        {
            sortedCollection.setSortedField(getSortField(), isSortDescending());
        }
    }

    public String getSortField( )
    {
        return sortField;
    }

    public void setSortField(String sortField)
    {
        this.sortField = sortField;
        if(sortedCollection != null)
        {
            sortedCollection.setSortedField(getSortField(), isSortDescending());
        }
    }
}
