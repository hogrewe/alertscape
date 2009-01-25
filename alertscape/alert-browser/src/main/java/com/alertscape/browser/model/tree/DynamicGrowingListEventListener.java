/**
 * 
 */
package com.alertscape.browser.model.tree;

import com.alertscape.common.model.Alert;

import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

/**
 * @author josh
 *
 */
public class DynamicGrowingListEventListener implements ListEventListener<Alert> {
  
  public void listChanged(ListEvent<Alert> listEvent) {
  }
}
