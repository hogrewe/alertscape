/**
 * 
 */
package com.alertscape.browser.ui.swing.panel.collection.tree;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.Matcher;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 *
 */
public class TreeMatcherEditor extends AbstractMatcherEditor<Alert> {
  private Set<Alert> alerts = new HashSet<Alert>();
  
  public void addAlert(Alert a) {
    alerts.add(a);
    fireRelaxed(new TreeMatcher());
  }
  
  public void removeAlert(Alert a) {
    alerts.remove(a);
    fireConstrained(new TreeMatcher());
  }
  
  public void addAlerts(Collection<Alert> alerts) {
    this.alerts.addAll(alerts);
    fireRelaxed(new TreeMatcher());
  }
  
  public void clearAlerts() {
    this.alerts.clear();
    fireConstrained(new TreeMatcher());
  }
  
  public class TreeMatcher implements Matcher<Alert> {
    public boolean matches(Alert a) {
      return alerts.contains(a);
    }
  }
}
