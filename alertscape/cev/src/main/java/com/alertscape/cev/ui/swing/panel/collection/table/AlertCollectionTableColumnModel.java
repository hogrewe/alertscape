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
public class AlertCollectionTableColumnModel extends DefaultTableColumnModel
{
    private static final long serialVersionUID = 1L;
//    private static final Logger logger = Logger
//            .getLogger(EventCollectionTableColumnModel.class);
    private List<AlertColumn> eventColumns;
    private List<TableColumn> tableColumns;
    
    public AlertCollectionTableColumnModel(List<AlertColumn> columns)
    {
        this.eventColumns = columns;
        tableColumns = new ArrayList<TableColumn>();
        for(int i=0; i<columns.size(); i++)
        {
            AlertColumn c = columns.get(i);
            TableColumn tc = buildTableColumn(c);
            tc.setModelIndex(i);
            tableColumns.add(i, tc);
            addColumn(tc);
        }
    }

    public void addEventColumn(AlertColumn column)
    {
        eventColumns.add(0, column);
        TableColumn tc = buildTableColumn(column);
        tableColumns.add(0, tc);
        addColumn(tc);
    }
    
    public void addEventColumn(AlertColumn column, int index)
    {
        eventColumns.add(index, column);
        TableColumn tc = buildTableColumn(column);
        tableColumns.add(index, tc);
        addColumn(tc);
    }

    protected TableColumn buildTableColumn(AlertColumn column)
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
