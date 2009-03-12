package com.alertscape.wizard.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class InstallWizard implements EntryPoint {
  public void onModuleLoad() {
    final InstallWizardServiceAsync wizardService = GWT.create(InstallWizardService.class);

    final SimplePanel main = new SimplePanel();
    final DecoratorPanel finishPanel = new DecoratorPanel();
    finishPanel.setWidget(new Label("Install Complete"));
    
    List<WizardStep> steps = new ArrayList<WizardStep>();
    steps.add(new WizardStep("Choose Home Directory", new HomeDirWidget()));
    steps.add(new WizardStep("Set DB Info", new DBInfoWidget()));
    steps.add(new WizardStep("Set Server Info", new ServerInfoWidget()));
    
    Wizard wiz = new Wizard(steps, new WizardFinishHandler() {
      public void handleFinish() {
        wizardService.writeInstallFiles(new AsyncCallback<Void>() {
          public void onFailure(Throwable t) {
            finishPanel.setWidget(new Label("Install failed! " + t));
            main.setWidget(finishPanel);
          }

          public void onSuccess(Void value) {
            finishPanel.setWidget(new Label("Installation success!"));
            main.setWidget(finishPanel);            
          }
        });
      }
    });
    
    main.setWidget(wiz);
    
    RootPanel.get("install").add(main);
  }
}
