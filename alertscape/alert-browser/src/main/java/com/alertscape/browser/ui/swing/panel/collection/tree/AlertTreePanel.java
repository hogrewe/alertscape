/**
 * 
 */
package com.alertscape.browser.ui.swing.panel.collection.tree;

import java.awt.BorderLayout;
import java.util.Comparator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import ca.odell.glazedlists.CompositeList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.UniqueList;
import ca.odell.glazedlists.event.ListEventPublisher;
import ca.odell.glazedlists.matchers.Matchers;

import com.alertscape.browser.model.AlertFilter;
import com.alertscape.browser.model.tree.AlertTreeNode;
import com.alertscape.browser.model.tree.DefaultAlertTreeNode;
import com.alertscape.browser.ui.swing.tree.AlertTree;
import com.alertscape.browser.ui.swing.tree.AlertTreeModel;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.BinarySortAlertCollection;

/**
 * @author josh
 * 
 */
public class AlertTreePanel extends JPanel implements AlertFilter {
  private static final long serialVersionUID = 3210775898472971525L;
  private AlertCollection subCollection;
  private CompositeList<Alert> compositeList;
  private AlertTree alertTree;
  private AlertTreeNode root;

  public void init() {
    root = new DefaultAlertTreeNode();
    root.setText("All Alerts");
    root.setIcon("/com/alertscape/images/common/as_logo2_16.png");

    DefaultAlertTreeNode sources = new DefaultAlertTreeNode("Sources");
    root.addChild(sources);

    DefaultAlertTreeNode intermapper = new DefaultAlertTreeNode("InterMapper");
    intermapper.setIcon("/com/alertscape/images/tree/intermapper.png");
    intermapper.setMatcher(Matchers.beanPropertyMatcher(Alert.class, "source", new AlertSource(6, "INTERMAPPER")));
    sources.addChild(intermapper);

    DefaultAlertTreeNode kyriaki = new DefaultAlertTreeNode("Kyriaki");
    kyriaki.setIcon("/com/alertscape/images/tree/kyriaki.png");
    kyriaki.setMatcher(Matchers.beanPropertyMatcher(Alert.class, "source", new AlertSource(2, "KYRIAKI_NET")));
    sources.addChild(kyriaki);
    
    DefaultAlertTreeNode types = new DefaultAlertTreeNode("Types");
    root.addChild(types);
    
    DefaultAlertTreeNode utilGt50 = new DefaultAlertTreeNode("util >= 50");
    utilGt50.setMatcher(Matchers.beanPropertyMatcher(Alert.class, "type", "util >= 50"));
    types.addChild(utilGt50);

    DefaultAlertTreeNode utilGt90 = new DefaultAlertTreeNode("util >= 90");
    utilGt90.setMatcher(Matchers.beanPropertyMatcher(Alert.class, "type", "util >= 90"));
    types.addChild(utilGt90);

    DefaultAlertTreeNode linkDown = new DefaultAlertTreeNode("LINK DOWN");
    linkDown.setMatcher(Matchers.beanPropertyMatcher(Alert.class, "type", "LINK DOWN"));
    types.addChild(linkDown);

    alertTree = new AlertTree(new AlertTreeModel(root));

    JScrollPane treePane = new JScrollPane(alertTree);

    alertTree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        for (TreePath treePath : e.getPaths()) {
          AlertTreeNode selectedNode = (AlertTreeNode) treePath.getLastPathComponent();

          AlertCollection collection = selectedNode.getCollection();
          EventList<Alert> nodeList = collection.getEventList();

          if (e.isAddedPath(treePath)) {
            ListEventPublisher publisher = nodeList.getPublisher();
            compositeList.addMemberList(nodeList);
          } else {
            compositeList.removeMemberList(nodeList);
          }
        }
      }
    });

    setLayout(new BorderLayout());
    add(treePane, BorderLayout.CENTER);
  }

  public AlertCollection setMasterCollection(AlertCollection master) {
    AlertCollection rootCollection = root.setMasterCollection(master);
    EventList<Alert> rootList = rootCollection.getEventList();
    compositeList = new CompositeList<Alert>(rootList.getPublisher(), rootList.getReadWriteLock());
    // compositeList.addMemberList(rootList);
    UniqueList<Alert> uniqueList = new UniqueList<Alert>(compositeList, new Comparator<Alert>() {
      public int compare(Alert o1, Alert o2) {

        return (int) (o1.getCompositeAlertId() - o2.getCompositeAlertId());
      }
    });
    subCollection = new BinarySortAlertCollection(uniqueList);
    
    alertTree.setSelectionRow(0);

    return subCollection;
  }
}
