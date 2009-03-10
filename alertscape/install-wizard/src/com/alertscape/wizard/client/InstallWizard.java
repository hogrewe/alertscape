package com.alertscape.wizard.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class InstallWizard implements EntryPoint {
  public void onModuleLoad() {
    List<WizardStep> steps = new ArrayList<WizardStep>();
    steps.add(new WizardStep("Choose Home Directory", new HomeDirWidget()));
    steps.add(new WizardStep("Set DB Info", new SetDBInfoWidget()));
    
    Wizard wiz = new Wizard(steps );
    RootPanel.get("install").add(wiz);
  }
}
