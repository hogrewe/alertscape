/**
 * 
 */
package com.alertscape.browser.ui.swing.panel.collection.filter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.impl.beans.BeanTextFilterator;
import ca.odell.glazedlists.matchers.SearchEngineTextMatcherEditor.Field;
import ca.odell.glazedlists.swing.SearchEngineTextFieldMatcherEditor;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

import com.alertscape.browser.localramp.firstparty.preferences.UserPreferencesPanel;
import com.alertscape.browser.model.AlertFilter;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;
import com.alertscape.common.model.BinarySortAlertCollection;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author josh
 * 
 */
public class TextFilterPanel extends JPanel implements AlertFilter, UserPreferencesPanel {
  private static final long serialVersionUID = -235853694487718263L;

  private AlertCollection subCollection;
  private TextComponentMatcherEditor<Alert> simpleMatcherEditor;
  private SearchEngineTextFieldMatcherEditor<Alert> searchEngineMatcherEditor;
  private JComboBox searchCombo;
  private JTextField searchText;

  private FilterList<Alert> filterList;

  private TextFieldListener fieldListener;

  public TextFilterPanel() {
    init();
  }

  public void init() {
    setLayout(new BorderLayout());
    searchText = new JTextField();
    searchText.setMinimumSize(new Dimension(100, 25));
    searchText.setPreferredSize(new Dimension(300, 25));

    fieldListener = new TextFieldListener(searchText);
    fieldListener.setLive(true);

    searchCombo = new JComboBox(new Object[] { "Quick", "Advanced" });
    searchCombo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (searchCombo.getSelectedItem().equals("Advanced")) {
          searchEngineMatcherEditor.refilter(searchText.getText());
          filterList.setMatcherEditor(searchEngineMatcherEditor);
          fieldListener.setLive(false);
        } else {
          filterList.setMatcherEditor(simpleMatcherEditor);
          fieldListener.setLive(true);
        }
      }
    });

    // searchText.setBorder(BorderFactory.createLineBorder(Color.lightGray)); // lame - for some reason this hoses the
    // margin, so I commented it out
    searchText.setMargin(new Insets(1, 3, 1, 1));
    FormLayout layout = new FormLayout("right:pref, 3dlu, default:grow", "");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.append(searchCombo, searchText);

    add(builder.getPanel(), BorderLayout.CENTER);

    String[] propertyNames = { "shortDescription", "longDescription", "type", "item", "itemManager", "itemType",
        "itemManagerType", "severity", "source", "majorTags", "minorTags" };

    // TODO: Create Alert filterator with the attributes and categories
    TextFilterator<Alert> textFilterator = GlazedLists.textFilterator(propertyNames);
    simpleMatcherEditor = new TextComponentMatcherEditor<Alert>(searchText, textFilterator);
    searchEngineMatcherEditor = new SearchEngineTextFieldMatcherEditor<Alert>(searchText, textFilterator);
    Set<Field<Alert>> fields = new HashSet<Field<Alert>>();
    fields.add(new Field<Alert>("description",
        new BeanTextFilterator<Object, Alert>(Alert.class, "longDescription")));
    fields.add(new Field<Alert>("type", new BeanTextFilterator<Object, Alert>(Alert.class, "type")));
    fields.add(new Field<Alert>("item", new BeanTextFilterator<Object, Alert>(Alert.class, "item")));
    fields.add(new Field<Alert>("itemManager", new BeanTextFilterator<Object, Alert>(Alert.class, "itemManager")));
    fields.add(new Field<Alert>("itemType", new BeanTextFilterator<Object, Alert>(Alert.class, "itemType")));
    fields.add(new Field<Alert>("itemManagerType",
        new BeanTextFilterator<Object, Alert>(Alert.class, "itemManagerType")));
    fields.add(new Field<Alert>("severity", new BeanTextFilterator<Object, Alert>(Alert.class, "severity")));
    fields.add(new Field<Alert>("source", new BeanTextFilterator<Object, Alert>(Alert.class, "source")));
    searchEngineMatcherEditor.setFields(fields);
    // matcherEditor.setLive(false);
  }

  public AlertCollection setMasterCollection(AlertCollection master) {
    EventList<Alert> masterList = master.getEventList();
    filterList = new FilterList<Alert>(masterList, simpleMatcherEditor);
    subCollection = new BinarySortAlertCollection(filterList);

    return subCollection;
  }

  public Map getUserPreferences() {
    Map map = new HashMap();
    map.put("QuickFilter", searchText.getText());
    return map;
  }

  public void setUserPreferences(Map preferences) {
    String filter = (String) preferences.get("QuickFilter");
    searchText.setText(filter);
  }

  private static class TextFieldListener implements DocumentListener, ActionListener {

    private static final Color NEEDS_SEARCH_COLOR = new Color(255,255,100);
    private static final Color DOESNT_NEED_SEARCH_COLOR = Color.white;

    private boolean _needsSearch;
    private boolean live;
    private JTextField textField;

    private TextFieldListener(JTextField textField) {
      this.textField = textField;
      textField.addActionListener(this);
      textField.getDocument().addDocumentListener(this);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
      if (!live) {
        updateTextField(true);
      }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
      if (!live) {
        updateTextField(true);
      }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
      if (!live) {
        updateTextField(true);
      }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (!live) {
        updateTextField(false);
      }
    }

    public void setLive(boolean live) {
      this.live = live;
      updateTextField(false);
    }

    private void updateTextField(boolean needsSearch) {
      if (needsSearch != _needsSearch) {
        if (needsSearch) {
          textField.setBackground(NEEDS_SEARCH_COLOR);
        } else {
          textField.setBackground(DOESNT_NEED_SEARCH_COLOR);
        }
        _needsSearch = needsSearch;
      }
    }

  }
}
