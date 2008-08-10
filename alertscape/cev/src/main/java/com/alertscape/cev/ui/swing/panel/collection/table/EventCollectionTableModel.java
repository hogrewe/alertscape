/*
 * Created on Apr 1, 2006
 */
package com.alertscape.cev.ui.swing.panel.collection.table;

import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.alertscape.cev.model.EventChange;
import com.alertscape.cev.model.panel.EventCollectionPanelModel;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;

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

  private AlertCollection collection;

  public EventCollectionTableModel(List<EventColumn> columns)
  {
    setColumns(columns);
  }

  public void setCollection(AlertCollection collection)
  {
    this.collection = collection;
  }

  public AlertCollection getCollection( )
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
    return getCollection( ).getAlertCount( );
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
    Class<?> c = Object.class;
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
    Alert e = collection.getAlertAt(rowIndex);
    return column.getValue(e);
  }

  public Alert getEventAt(int rowIndex)
  {
    return collection.getAlertAt(rowIndex);
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
