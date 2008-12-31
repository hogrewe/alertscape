/**
 * 
 */
package com.alertscape.browser.ui.swing.panel.collection.filter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

import com.alertscape.browser.model.AlertFilter;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;
import com.alertscape.common.model.IndexedAlertCollection;

/**
 * @author josh
 * 
 */
public class TextFilterPanel extends JPanel implements AlertFilter {
  private static final long serialVersionUID = -235853694487718263L;

  private AlertCollection subCollection;
  private TextComponentMatcherEditor<Alert> matcherEditor;
  private JTextField searchText;
  
  public TextFilterPanel() {
    init();
  }

  public void init() {
    //setLayout(new GridBagLayout());
  	//setLayout(new FlowLayout());
  	//setLayout(new BorderLayout());
  	setLayout(new GridLayout(1,1));
    JPanel searchPanel = new JPanel();
    searchText = new JTextField();
    searchText.setMinimumSize(new Dimension(100,20));
    searchText.setPreferredSize(new Dimension(300,20));    
    searchPanel.add(searchText);
    
    add(searchPanel);

    String[] propertyNames = { "shortDescription", "longDescription", "type", "item", "itemManager", "itemType",
        "itemManagerType",  "severity", "source"};
    TextFilterator<Alert> textFilterator = GlazedLists.textFilterator(propertyNames);
    matcherEditor = new TextComponentMatcherEditor<Alert>(searchText, textFilterator);
//    matcherEditor.setLive(false);
  }

  public AlertCollection setMasterCollection(AlertCollection master) {
    EventList<Alert> masterList = master.getEventList();
    FilterList<Alert> filterList = new FilterList<Alert>(masterList, matcherEditor);
    subCollection = new IndexedAlertCollection(filterList);

    return subCollection;
  }

}
