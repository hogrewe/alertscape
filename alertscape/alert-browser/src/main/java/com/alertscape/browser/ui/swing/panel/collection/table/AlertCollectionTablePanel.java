/*
 * Created on Apr 1, 2006
 */

package com.alertscape.browser.ui.swing.panel.collection.table;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;

import com.alertscape.browser.localramp.firstparty.preferences.UserPreferencesPanel;
import com.alertscape.browser.ui.swing.panel.collection.AlertCollectionPanel;
import com.alertscape.browser.ui.swing.panel.collection.table.renderer.DateAlertCellRenderer;
import com.alertscape.browser.ui.swing.panel.collection.table.renderer.DefaultAlertCellRenderer;
import com.alertscape.browser.ui.swing.panel.collection.table.renderer.SeverityAlertCellRenderer;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertAttributeDefinition;
import com.alertscape.common.model.AlertCollection;
import com.alertscape.common.model.severity.Severity;

/**
 * @author josh
 * @version $Version: $
 */
public class AlertCollectionTablePanel extends JPanel implements AlertCollectionPanel, UserPreferencesPanel
{
  private static final long serialVersionUID = 1L;
  private static final String PREFERENCES_COL_WIDTHS = "ColumnWidths";

  // member variables
  private JTable collectionTable;
  private AlertCollection collection;
  private SortedList<Alert> sortedList;
  private List<AlertAttributeDefinition> extendedAttributes;

  private JPopupMenu popup;
  private TableComparatorChooser<Alert> chooser;
  private TableFormat<Alert> tf;
  private EventTableModel<Alert> model;

  public AlertCollectionTablePanel(AlertCollection collection) {
    setCollection(collection);
  }

  public AlertCollectionTablePanel(AlertCollection collection, List<AlertAttributeDefinition> extendedAttributes) {
    this(collection);
    setExtendedAttributes(extendedAttributes);
  }

  public void init() {
    sortedList = new SortedList<Alert>(getCollection().getEventList(), null);
    // String[] propertyNames = new String[] { "alertId", "type",
    // "longDescription", "severity", "count", "source", "item",
    // "itemManager", "itemType", "itemManagerType", "firstOccurence",
    // "lastOccurence" };
    // String[] columnLabels = new String[] { "Alert ID", "Type", "Description",
    // "Severity", "Count", "Source", "Item", "Manager", "Item Type",
    // "Manager Type", "First", "Last" };

    List<String> propertyNames = new ArrayList<String>();
    propertyNames.add("lastOccurence");
    propertyNames.add("firstOccurence");
    propertyNames.add("type");
    propertyNames.add("item");
    propertyNames.add("severity");
    propertyNames.add("longDescription");
    propertyNames.add("acknowledgedBy");
    propertyNames.add("count");
    propertyNames.add("itemManager");
    propertyNames.add("itemType");
    propertyNames.add("itemManagerType");
    propertyNames.add("compositeAlertId");
    propertyNames.add("source");

    List<String> columnLabels = new ArrayList<String>();
    columnLabels.add("Last Event");
    columnLabels.add("First Event");
    columnLabels.add("Type");
    columnLabels.add("Item");
    columnLabels.add("Severity");
    columnLabels.add("Description");
    columnLabels.add("Acknowledged By");
    columnLabels.add("Count");
    columnLabels.add("Manager");
    columnLabels.add("Item Type");
    columnLabels.add("Manager Type");
    columnLabels.add("Alert ID");
    columnLabels.add("Source");

    Integer[] columnWidths = new Integer[] {
    		130, 
    		130, 
    		100,
    		100, 
    		50, 
    		300, 
        100, 
    		50, 
    		100, 
    		100,
    		100, 
    		100, 
    		100 };	
                                        

    tf = new AlertTableFormat(propertyNames, columnLabels, extendedAttributes);
    model = new EventTableModel<Alert>(sortedList, tf);
    collectionTable = new JTable(model);

    chooser = new TableComparatorChooser<Alert>(collectionTable, sortedList, true);

    TableCellRenderer defaultRenderer = new DefaultAlertCellRenderer();
    TableCellRenderer sevRenderer = new SeverityAlertCellRenderer();
    TableCellRenderer dateRenderer = new DateAlertCellRenderer();

    collectionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    collectionTable.setDefaultRenderer(Long.class, defaultRenderer);
    collectionTable.setDefaultRenderer(Object.class, defaultRenderer);
    collectionTable.setDefaultRenderer(Date.class, dateRenderer);
    collectionTable.setDefaultRenderer(Severity.class, sevRenderer);
    collectionTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    setLayout(new BorderLayout( ));
    JScrollPane tableScroller = new JScrollPane(collectionTable);
    add(tableScroller, BorderLayout.CENTER);
    
    // set up the table column widths to their default sizes
		TableColumn column = null;
		for (int i = 0; i < collectionTable.getColumnCount(); i++) 
		{
			column = collectionTable.getColumnModel().getColumn(i);
			column.setPreferredWidth(columnWidths[i]); 
		}
  }

  public void setPopup(JPopupMenu popper)
  {
    //Add listener to components that can bring up popup menus.
  	popup = popper;
  	MouseListener popupListener = new PopupListener();
    collectionTable.addMouseListener(popupListener);	
  }
  
  //
  // public void oldInit( )
  // {
  // List<EventColumn> columns = new ArrayList<EventColumn>( );
  // EventColumn c;
  // c = new EventColumn("Event ID", "eventId");
  // columns.add(c);
  // c = new EventColumn("Description", "longDescription");
  // columns.add(c);
  // c = new EventColumn("Type", "type");
  // columns.add(c);
  // c = new EventColumn("Severity", "severity");
  // columns.add(c);
  // c = new EventColumn("Count", "count");
  // columns.add(c);
  // c = new EventColumn("Source", "sourceId");
  // columns.add(c);
  // c = new EventColumn("Item", "item");
  // c.setWidth(200);
  // columns.add(c);
  // c = new EventColumn("Manager", "itemManager");
  // c.setWidth(200);
  // columns.add(c);
  // c = new EventColumn("Item Type", "itemType");
  // c.setWidth(200);
  // columns.add(c);
  // c = new EventColumn("Manager Type", "itemManagerType");
  // c.setWidth(200);
  // columns.add(c);
  // c = new EventColumn("First", "firstOccurence");
  // c.setWidth(250);
  // columns.add(c);
  // c = new EventColumn("Last", "lastOccurence");
  // c.setWidth(250);
  // columns.add(c);
  //
  // model = new EventCollectionTableModel(columns);
  // EventCollectionTableColumnModel columnModel = new
  // EventCollectionTableColumnModel(
  // columns);
  // // collectionTable = new JTable(model, columnModel);
  // // collectionTable = new JTable(model);
  // collectionTable = new EventTable(model, columnModel);
  // sorter = new TableRowSorter<EventCollectionTableModel>(model);
  //
  // TableCellRenderer defaultRenderer = new DefaultEventCellRenderer( );
  // TableCellRenderer sevRenderer = new SeverityEventCellRenderer( );
  //
  // collectionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
  // collectionTable.setRowSorter(sorter);
  // collectionTable.setDefaultRenderer(Long.class, defaultRenderer);
  // collectionTable.setDefaultRenderer(Object.class, defaultRenderer);
  // collectionTable.setDefaultRenderer(Date.class, defaultRenderer);
  // collectionTable.setDefaultRenderer(Severity.class, sevRenderer);
  // collectionTable
  // .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
  // // collectionTable.setRowSelectionAllowed(true);
  // setLayout(new BorderLayout( ));
  // JScrollPane tableScroller = new JScrollPane(collectionTable);
  // add(tableScroller, BorderLayout.CENTER);
  // }

  /**
   * @return Returns the collection.
   */
  public AlertCollection getCollection() {
    return collection;
  }

  public List<Alert> getSelectedAlerts() {
    int[] myrows = collectionTable.getSelectedRows();
    ArrayList<Alert> retval = new ArrayList<Alert>(myrows.length);

    for (int i = 0; i < myrows.length; i++) {
      int nextrow = myrows[i];

      // Alert next = collection.getAlertAt(converted);
      Alert next = sortedList.get(nextrow);
      retval.add(next);
    }

    return retval;
  }

  /**
   * @param collection
   *          The collection to set.
   */
  private void setCollection(AlertCollection collection) {
    this.collection = collection;
  }

  /**
   * @return the extendedAttributes
   */
  public List<AlertAttributeDefinition> getExtendedAttributes() {
    return extendedAttributes;
  }

  /**
   * @param extendedAttributes
   *          the extendedAttributes to set
   */
  public void setExtendedAttributes(List<AlertAttributeDefinition> extendedAttributes) {
    this.extendedAttributes = extendedAttributes;
  }

  // public void setCollection(EventCollection collection)
  // {
  // model.setCollection(collection);
  // }
  //
  // public EventCollection getCollection( )
  // {
  // return model.getCollection( );
  // }

	public Map getUserPreferences()
	{
		// build a map on the fly, based on what the user current has selected in this panel
		// columns shown
		// column order
		// column widths
		// sorting choices
		Map map = new HashMap();
		
		// get the column widths
    TableColumn column = null;
    int columncount = collectionTable.getColumnCount();
    ArrayList<Integer> widths = new ArrayList<Integer> (columncount);
		for (int i = 0; i < columncount; i++) 
		{
			// get the next column
			column = collectionTable.getColumnModel().getColumn(i);
						
			// get and store the current width of the column
			Integer width = new Integer(column.getWidth());
			widths.add(width);
		}
		map.put(PREFERENCES_COL_WIDTHS, widths);
		
		return map;
	}

	public void setUserPreferences(Map preferences)
	{
		// TODO Auto-generated method stub
		
		// set the column widths: this assumes that the columns have already been added, and are in the correct order!
    TableColumn column = null;
    int columncount = collectionTable.getColumnCount();
    ArrayList<Integer> widths = (ArrayList<Integer>)preferences.get(PREFERENCES_COL_WIDTHS);
		for (int i = 0; i < columncount; i++) 
		{
			column = collectionTable.getColumnModel().getColumn(i);
			Integer width = widths.get(i);
		
			// set the width of the column to the value from the preferences map
			column.setPreferredWidth(width);
			column.setWidth(width);
		}
	}
  
  class PopupListener extends MouseAdapter {
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popup.show(e.getComponent(),
                       e.getX(), e.getY());
        }
    }

}

}
