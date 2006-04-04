/*
 * Created on Apr 3, 2006
 */
package com.alertscape.cev.ui.swing.panel.table;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * @author josh
 * @version $Version: $
 */
public class EventTable extends JTable
{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public EventTable( )
    {
        super( );
    }

    /**
     * @param dm
     * @param cm
     * @param sm
     */
    public EventTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm)
    {
        super(dm, cm, sm);
    }

    /**
     * @param dm
     * @param cm
     */
    public EventTable(TableModel dm, TableColumnModel cm)
    {
        super(dm, cm);
    }

    /**
     * @param dm
     */
    public EventTable(TableModel dm)
    {
        super(dm);
    }

    @Override
    public void tableChanged(TableModelEvent e)
    {
        int[] viewRows = getSelectedRows( );
        int[] rows = new int[viewRows.length];
        for (int i = 0; i < viewRows.length; i++)
        {
            rows[i] = convertRowIndexToModel(viewRows[i]);
        }
        super.tableChanged(e);
        getSelectionModel( ).setValueIsAdjusting(true);
        for (int i = 0; i < rows.length; i++)
        {
            int sel = convertRowIndexToView(rows[i]);
            getSelectionModel( ).addSelectionInterval(sel, sel);
        }
        getSelectionModel( ).setValueIsAdjusting(false);
    }

    
}
