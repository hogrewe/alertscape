/*
 * Created on Apr 2, 2006
 */
package com.alertscape.cev.ui.swing.panel.collection.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

/**
 * @author josh
 * @version $Version: $
 */
public class EventCollectionTableColumnModel extends DefaultTableColumnModel
{
    private static final long serialVersionUID = 1L;
//    private static final Logger logger = Logger
//            .getLogger(EventCollectionTableColumnModel.class);
    private List<EventColumn> eventColumns;
    private List<TableColumn> tableColumns;
    
    public EventCollectionTableColumnModel(List<EventColumn> columns)
    {
        this.eventColumns = columns;
        tableColumns = new ArrayList<TableColumn>();
        for(int i=0; i<columns.size(); i++)
        {
            EventColumn c = columns.get(i);
            TableColumn tc = buildTableColumn(c);
            tc.setModelIndex(i);
            tableColumns.add(i, tc);
            addColumn(tc);
        }
    }

    public void addEventColumn(EventColumn column)
    {
        eventColumns.add(0, column);
        TableColumn tc = buildTableColumn(column);
        tableColumns.add(0, tc);
        addColumn(tc);
    }
    
    public void addEventColumn(EventColumn column, int index)
    {
        eventColumns.add(index, column);
        TableColumn tc = buildTableColumn(column);
        tableColumns.add(index, tc);
        addColumn(tc);
    }

    protected TableColumn buildTableColumn(EventColumn column)
    {
        TableColumn tc = new TableColumn();
        
        tc.setHeaderValue(column.getDisplayName());
        if(column.getMinWidth() > 0)
        {
            tc.setMinWidth(column.getMinWidth());
        }
        if(column.getMaxWidth() > 0)
        {
            tc.setMaxWidth(column.getMaxWidth());
        }
        if(column.getWidth() > 0)
        {
            tc.setPreferredWidth(column.getWidth());
        }
        return tc;
    }
}
