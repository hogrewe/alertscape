/*
 * Created on Apr 1, 2006
 */
package com.alertscape.browser.ui.swing.panel.collection.table;

import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.alertscape.browser.model.AlertChange;
import com.alertscape.browser.model.panel.AlertCollectionPanelModel;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;

/**
 * @author josh
 * @version $Version: $
 */
public class AlertCollectionTableModel extends AbstractTableModel implements
    AlertCollectionPanelModel
{
  // private static Logger logger = Logger
  // .getLogger(EventCollectionTableModel.class);

  private static final long serialVersionUID = 1L;
  private List<AlertColumn> columns;

  private AlertCollection collection;

  public AlertCollectionTableModel(List<AlertColumn> columns)
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

  public void handleChange(AlertChange change)
  {
    List<Integer> indices = change.getIndices( );
    for (int i = 0, size = indices.size( ); i < size; i++)
    {
      int index = indices.get(i);
      // TODO: make this smarter and send ranges
      if (change.getType( ) == AlertChange.AlertChangeType.INSERT)
      {
        fireTableRowsInserted(index, index);
      }
      else if (change.getType( ) == AlertChange.AlertChangeType.UPDATE)
      {
        fireTableRowsUpdated(index, index);
      }
      else if (change.getType( ) == AlertChange.AlertChangeType.REMOVE)
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
    AlertColumn column = columns.get(columnIndex);
    if (column.getPropertyGetter( ) != null)
    {
      c = column.getColumnClass( );
    }
    return c;
  }

  public Object getValueAt(int rowIndex, int columnIndex)
  {
    AlertColumn column = columns.get(columnIndex);
    Alert e = collection.getAlertAt(rowIndex);
    return column.getValue(e);
  }

  public Alert getEventAt(int rowIndex)
  {
    return collection.getAlertAt(rowIndex);
  }

  public void setColumns(List<AlertColumn> columns)
  {
    this.columns = columns;
  }

  public List<AlertColumn> getColumns( )
  {
    return Collections.unmodifiableList(columns);
  }
}
