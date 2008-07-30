/*
 * Created on Apr 1, 2006
 */
package com.alertscape.cev.ui.swing.panel.collection.table;

import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.alertscape.cev.model.Event;
import com.alertscape.cev.model.EventChange;
import com.alertscape.cev.model.EventCollection;
import com.alertscape.cev.model.panel.EventCollectionPanelModel;

/**
 * @author josh
 * @version $Version: $
 */
public class EventCollectionTableModel extends AbstractTableModel implements
    EventCollectionPanelModel
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
  }

  public EventCollection getCollection( )
  {
    return collection;
  }

  public void handleChange(EventChange change)
  {
    List<Integer> indices = change.getIndices( );
    for (int i = 0, size = indices.size( ); i < size; i++)
    {
      int index = indices.get(i);
      // TODO: make this smarter and send ranges
      if (change.getType( ) == EventChange.EventChangeType.INSERT)
      {
        fireTableRowsInserted(index, index);
      }
      else if (change.getType( ) == EventChange.EventChangeType.UPDATE)
      {
        fireTableRowsUpdated(index, index);
      }
      else if (change.getType( ) == EventChange.EventChangeType.REMOVE)
      {
        fireTableRowsDeleted(index, index);
      }
    }
  }

  public int getRowCount( )
  {
    return getCollection( ).getEventCount( );
  }

  public int getColumnCount( )
  {
    return columns.size( );
  }

  @Override
  public String getColumnName(int columnIndex)
  {
    return columns.get(columnIndex).getDisplayName( );
  }

  @Override
  public Class<?> getColumnClass(int columnIndex)
  {
    Class c = Object.class;
    EventColumn column = columns.get(columnIndex);
    if (column.getPropertyGetter( ) != null)
    {
      c = column.getColumnClass( );
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
