/**
 * 
 */
package com.alertscape.browser.ui.swing.panel.collection.tree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.matchers.CompositeMatcherEditor;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.util.concurrent.Lock;

import com.alertscape.browser.model.AlertFilter;
import com.alertscape.browser.model.tree.AlertTreeNode;
import com.alertscape.browser.model.tree.DefaultAlertTreeNode;
import com.alertscape.browser.model.tree.DynamicGrowingAlertTreeNode;
import com.alertscape.browser.model.tree.NonEmptyAttributeMatcher;
import com.alertscape.browser.ui.swing.tree.AlertTreeModel;
import com.alertscape.browser.ui.swing.tree.AlertTreeNodeRenderer;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;
import com.alertscape.common.model.BinarySortAlertCollection;

/**
 * @author josh
 * 
 */
public class AlertTreePanel extends JPanel implements AlertFilter {
  private static final long serialVersionUID = 3210775898472971525L;
  private AlertCollection subCollection;
  private JTree alertTree;
  private AlertTreeNode root;
  private CompositeMatcherEditor<Alert> compositeEditor;
  private List<Alert> existingEvents = new ArrayList<Alert>();
  private AlertTreeModel treeModel;

  public void init() {
    root = new DefaultAlertTreeNode();
    root.setText("All Alerts");
    root.setIcon("/com/alertscape/images/common/as_logo2_16.png");

    DynamicGrowingAlertTreeNode sources = new DynamicGrowingAlertTreeNode();
    sources.setText("Sources");
    sources.setDynamicPath("source");
    root.addChild(sources);

    DynamicGrowingAlertTreeNode itemDyn = new DynamicGrowingAlertTreeNode();
    itemDyn.setText("Items");
    itemDyn.setMatcher(new NonEmptyAttributeMatcher("stateprovince"));
    itemDyn.setDynamicPath("cat{stateprovince}:cat{city}:item:type");
    root.addChild(itemDyn);

    treeModel = new AlertTreeModel(root);
    alertTree = new JTree(treeModel);
    alertTree.setCellRenderer(new AlertTreeNodeRenderer());
    alertTree.setMinimumSize(new Dimension(300, 200));

    JScrollPane treePane = new JScrollPane(alertTree);
    treePane.setBorder(BorderFactory.createEmptyBorder());
    //treePane.setViewportBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    treePane.setViewportBorder(BorderFactory.createMatteBorder(5,5,5,5, alertTree.getBackground()));

    compositeEditor = new CompositeMatcherEditor<Alert>();

    alertTree.addTreeSelectionListener(new TreeSelectionListener() {
      @SuppressWarnings("unchecked")
      public void valueChanged(TreeSelectionEvent e) {
        for (TreePath treePath : e.getPaths()) {
          DefaultAlertTreeNode selectedNode = (DefaultAlertTreeNode) treePath.getLastPathComponent();

          EventList<MatcherEditor<Alert>> matcherEditors = compositeEditor.getMatcherEditors();
          if (e.isAddedPath(treePath)) {
            matcherEditors.add(selectedNode.getMatcherEditor());
          } else {
            matcherEditors.remove(selectedNode.getMatcherEditor());
          }
        }
      }
    });

    setLayout(new BorderLayout());
    add(treePane, BorderLayout.CENTER);
  }

  public AlertCollection setMasterCollection(AlertCollection master) {
    EventList<Alert> rootList = master.getEventList();
    FilterList<Alert> filterList = new FilterList<Alert>(rootList, compositeEditor);
    subCollection = new BinarySortAlertCollection(filterList);
    existingEvents.clear();
    existingEvents.addAll(rootList);

    rootList.addListEventListener(new ListEventListener<Alert>() {
      public void listChanged(ListEvent<Alert> listChanges) {
        EventList<Alert> list = listChanges.getSourceList();
        Lock lock = list.getReadWriteLock().readLock();
        lock.lock();
        Alert alert;
        while (listChanges.next()) {
          int index = listChanges.getIndex();
          switch (listChanges.getType()) {
          case ListEvent.INSERT:
            alert = list.get(index);
            existingEvents.add(index, alert);
            root.addAlert(alert);
            break;
          case ListEvent.DELETE:
            alert = existingEvents.remove(index);
            root.removeAlert(alert);
            break;
          case ListEvent.UPDATE:
            alert = existingEvents.get(index);
            Alert newEvent = list.get(index);
            existingEvents.set(index, newEvent);
            root.addAlert(newEvent);
            break;
          }

        }

        lock.unlock();
      }
    });

    // Initialize the counts to the current counts in the collection
    Lock lock = rootList.getReadWriteLock().readLock();
    lock.lock();
    for (Alert alert : rootList) {
      root.addAlert(alert);
    }
    lock.unlock();

    alertTree.setSelectionRow(0);

    return subCollection;
  }
}
