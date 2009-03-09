package com.alertscape.wizard.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class InstallWizard implements EntryPoint {
  public void onModuleLoad() {
    RootPanel.get("install").add(new HomeDirWidget());
  }
}
