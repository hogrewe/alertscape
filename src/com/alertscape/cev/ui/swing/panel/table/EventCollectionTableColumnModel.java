/*
 * Created on Apr 2, 2006
 */
package com.alertscape.cev.ui.swing.panel.table;

import java.util.Enumeration;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

/**
 * @author josh
 * @version $Version: $
 */
public class EventCollectionTableColumnModel extends DefaultTableColumnModel
{
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger
            .getLogger(EventCollectionTableColumnModel.class);

    @Override
    public void addColumn(TableColumn aColumn)
    {
        logger.debug("addColumn " + aColumn);
        super.addColumn(aColumn);
    }

    @Override
    public TableColumn getColumn(int columnIndex)
    {
        logger.debug("getColumn " + columnIndex);
        return super.getColumn(columnIndex);
    }

    @Override
    public int getColumnCount( )
    {
        logger.debug("getColumnCount");
        return super.getColumnCount( );
    }

    @Override
    public int getColumnIndex(Object identifier)
    {
        logger.debug("getColumnIndex " + identifier);
        return super.getColumnIndex(identifier);
    }

    @Override
    public int getColumnIndexAtX(int x)
    {
        logger.debug("getColumnIndexAtX " + x);
        return super.getColumnIndexAtX(x);
    }

    @Override
    public Enumeration<TableColumn> getColumns( )
    {
        logger.debug("getColumns");
        return super.getColumns( );
    }

}
