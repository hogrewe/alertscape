/*
 * Created on Apr 3, 2006
 */
package com.alertscape.cev.ui.swing.panel.collection.table;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.alertscape.common.logging.ASLogger;

/**
 * @author josh
 * @version $Version: $
 */
public class AlertTable extends JTable {
	private static final long serialVersionUID = 1L;

	/**
   * 
   */
	public AlertTable() {
		this(null);
	}

	/**
	 * @param dm
	 * @param cm
	 * @param sm
	 */
	public AlertTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
		super(dm, cm, sm);
	}

	/**
	 * @param dm
	 * @param cm
	 */
	public AlertTable(TableModel dm, TableColumnModel cm) {
		this(dm, cm, null);
		// this(dm, cm, new CustomListSelectionModel( ));
	}

	/**
	 * @param dm
	 */
	public AlertTable(TableModel dm) {
		this(dm, null);
	}

	// @Override
	// public void tableChanged(TableModelEvent e)
	// {
	// // bugs somewhere in here!!!
	// int[] viewRows = getSelectedRows( );
	// int[] rows = new int[viewRows.length];
	// for (int i = 0; i < viewRows.length; i++)
	// {
	// rows[i] = convertRowIndexToModel(viewRows[i]);
	// }
	// super.tableChanged(e);
	// getSelectionModel( ).setValueIsAdjusting(true);
	// for (int i = 0; i < rows.length; i++)
	// {
	// int sel = convertRowIndexToView(rows[i]);
	// System.out.println( );
	// System.out.println("row: " + rows[i] + ", old view row: " + viewRows[i]
	// + ", new view row: " + sel);
	// getSelectionModel( ).addSelectionInterval(sel, sel);
	// }
	// getSelectionModel( ).setValueIsAdjusting(false);
	// }

	@SuppressWarnings("unused")
	private static class CustomListSelectionModel implements ListSelectionModel {
		private ListSelectionModel delegate = new DefaultListSelectionModel();

		public void addListSelectionListener(ListSelectionListener x) {
			delegate.addListSelectionListener(x);
		}

		public void addSelectionInterval(int index0, int index1) {
			ASLogger.debug("addSelectionInterval(" + index0 + "," + index1
					+ ")");
			delegate.addSelectionInterval(index0, index1);
		}

		public void clearSelection() {
			ASLogger.debug("clearSelection()");
			delegate.clearSelection();
		}

		public int getAnchorSelectionIndex() {
			ASLogger.debug("getAnchorSelectionIndex()");
			return delegate.getAnchorSelectionIndex();
		}

		public int getLeadSelectionIndex() {
			// logger.debug("getLeadSelectionIndex()");
			return delegate.getLeadSelectionIndex();
		}

		public int getMaxSelectionIndex() {
			ASLogger.debug("getMaxSelectionIndex()");
			return delegate.getMaxSelectionIndex();
		}

		public int getMinSelectionIndex() {
			ASLogger.debug("getMinSelectionIndex()");
			return delegate.getMinSelectionIndex();
		}

		public int getSelectionMode() {
			ASLogger.debug("getSelectionMode()");
			return delegate.getSelectionMode();
		}

		public boolean getValueIsAdjusting() {
			return delegate.getValueIsAdjusting();
		}

		public void insertIndexInterval(int index, int length, boolean before) {
			ASLogger.debug("insertIndexInterval(" + index + "," + length + ","
					+ before + ")");
			delegate.insertIndexInterval(index, length, before);
		}

		public boolean isSelectedIndex(int index) {
			// logger.debug("isSelectedIndex(" + index + ")");
			return delegate.isSelectedIndex(index);
		}

		public boolean isSelectionEmpty() {
			ASLogger.debug("isSelectionEmpty()");
			return delegate.isSelectionEmpty();
		}

		public void removeIndexInterval(int index0, int index1) {
			ASLogger
					.debug("removeIndexInterval(" + index0 + "," + index1 + ")");
			delegate.removeIndexInterval(index0, index1);
		}

		public void removeListSelectionListener(ListSelectionListener x) {
			delegate.removeListSelectionListener(x);
		}

		public void removeSelectionInterval(int index0, int index1) {
			ASLogger.debug("removeSelectionInterval(" + index0 + "," + index1
					+ ")");
			delegate.removeSelectionInterval(index0, index1);
		}

		public void setAnchorSelectionIndex(int index) {
			ASLogger.debug("setAnchorSelectionIndex(" + index + ")");
			delegate.setAnchorSelectionIndex(index);
		}

		public void setLeadSelectionIndex(int index) {
			ASLogger.debug("setLeadSelectionIndex(" + index + ")");
			delegate.setLeadSelectionIndex(index);
		}

		public void setSelectionInterval(int index0, int index1) {
			ASLogger.debug("setSelectionInterval(" + index0 + "," + index1
					+ ")");
			delegate.setSelectionInterval(index0, index1);
		}

		public void setSelectionMode(int selectionMode) {
			ASLogger.debug("setSelectionMode(" + selectionMode + ")");
			delegate.setSelectionMode(selectionMode);
		}

		public void setValueIsAdjusting(boolean valueIsAdjusting) {
			ASLogger.debug("setValueIsAdjusting(" + valueIsAdjusting + ")");
			delegate.setValueIsAdjusting(valueIsAdjusting);
		}
	}
}