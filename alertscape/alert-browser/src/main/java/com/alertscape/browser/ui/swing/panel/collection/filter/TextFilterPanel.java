/**
 * 
 */
package com.alertscape.browser.ui.swing.panel.collection.filter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTextField;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

import com.alertscape.browser.localramp.firstparty.preferences.UserPreferencesPanel;
import com.alertscape.browser.model.AlertFilter;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;
import com.alertscape.common.model.BinarySortAlertCollection;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author josh
 * 
 */
public class TextFilterPanel extends JPanel implements AlertFilter, UserPreferencesPanel
{
  private static final long serialVersionUID = -235853694487718263L;

  private AlertCollection subCollection;
  private TextComponentMatcherEditor<Alert> matcherEditor;
  private JTextField searchText;
  
  public TextFilterPanel() {
    init();
  }

  public void init() {
    setLayout(new BorderLayout());
    searchText = new JTextField();
    searchText.setMinimumSize(new Dimension(100,20));
    searchText.setPreferredSize(new Dimension(300,20));
    FormLayout layout = new FormLayout("right:pref, 3dlu, default:grow", "");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.append("Quick Filter: ", searchText);

    add(builder.getPanel(), BorderLayout.CENTER);

    String[] propertyNames = { "shortDescription", "longDescription", "type", "item", "itemManager", "itemType",
        "itemManagerType",  "severity", "source"};
    TextFilterator<Alert> textFilterator = GlazedLists.textFilterator(propertyNames);
    matcherEditor = new TextComponentMatcherEditor<Alert>(searchText, textFilterator);
//    matcherEditor.setLive(false);
  }

  public AlertCollection setMasterCollection(AlertCollection master) {
    EventList<Alert> masterList = master.getEventList();
    FilterList<Alert> filterList = new FilterList<Alert>(masterList, matcherEditor);
    subCollection = new BinarySortAlertCollection(filterList);

    return subCollection;
  }
  
	public Map getUserPreferences()
	{
		Map map = new HashMap();
		map.put("QuickFilter", searchText.getText());
		return map;
	}

	public void setUserPreferences(Map preferences)
	{
		String filter = (String)preferences.get("QuickFilter");
		searchText.setText(filter);		
	}

}
