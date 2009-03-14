package com.alertscape.wizard.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Steps:
 * 
 * <ol>
 * <li>Set DB info
 * <ol>
 * <li>Check for access</li>
 * <li>Check for existing schema (warn and provide short circuit to finish if existing)</li>
 * <li>Install schema</li>
 * </ol>
 * </li>
 * <li>Choose home directory</li>
 * <li>Set server info</li>
 * <li>Setup onramps</li>
 * <li>Setup tree</li>
 * <li>Fin</li>
 * </ol>
 */
public class InstallWizard implements EntryPoint {
  public void onModuleLoad() {
    final InstallWizardServiceAsync wizardService = GWT.create(InstallWizardService.class);

    final SimplePanel main = new SimplePanel();
    final DecoratorPanel finishPanel = new DecoratorPanel();
    finishPanel.setWidget(new Label("Install Complete"));
    // finishPanel.addStyleName("install-wiz");

    InstallWizardInfo info = new InstallWizardInfo();

    List<WizardStep> steps = new ArrayList<WizardStep>();
    steps.add(new WizardStep("Set DB Info", new DBInfoWidget(wizardService, info)));
    steps.add(new WizardStep("Choose Home Directory", new HomeDirWidget(wizardService, info)));
    steps.add(new WizardStep("Set Server Info", new ServerInfoWidget(wizardService, info)));
    steps.add(new WizardStep("Install", new FinalInstallWidget(wizardService, info)));

    Wizard wiz = new Wizard(steps, new WizardFinishHandler() {
      public void handleFinish() {
        finishPanel.setWidget(new HTML("Installation success!<p>Please restart the application server."));
        main.setWidget(finishPanel);
      }
    });

    wiz.addStyleName("install-wiz");

    main.setWidget(wiz);

    RootPanel.get("install").add(main);
  }
}
