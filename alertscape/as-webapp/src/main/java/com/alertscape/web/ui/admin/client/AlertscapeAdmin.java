/**
 * 
 */
package com.alertscape.web.ui.admin.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author josh
 * 
 */
public class AlertscapeAdmin implements EntryPoint {

  public void onModuleLoad() {
    SimplePanel main = new SimplePanel();
    main.setWidget(new Label("Admin!!"));
    
    RootPanel.get("admin").add(main);
  }

}
