/**
 * 
 */
package com.alertscape.wizard.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author josh
 * 
 */
public class Wizard extends Composite implements WizardStateListener {

  private List<WizardStep> steps;
  private SimplePanel stepContentPanel;
  private Tree stepsTree;
  private Map<TreeItem, WizardStep> treeItemToStep;
  TreeItem[] treeItems;
  private int currentStep = -1;
  private Button prevButton;
  private Button nextButton;
  private WizardFinishHandler finisher;

  public Wizard(List<WizardStep> steps, WizardFinishHandler finisher) {
    this.finisher = finisher;
    treeItems = new TreeItem[steps.size()];
    VerticalPanel mainPanel = new VerticalPanel();
    initWidget(mainPanel);
    
    History.addHistoryListener(new HistoryListener() {
      public void onHistoryChanged(String historyToken) {
        int index = Integer.parseInt(historyToken);
        movetoStep(index);
      }
    });

    nextButton = new Button("Next", new NextClickHandler());
    
    prevButton = new Button("Previous", new PreviousClickHandler());

    stepContentPanel = new SimplePanel();
    stepsTree = new Tree();
    stepsTree.addTreeListener(new WizardTreeListener());
    
    HorizontalPanel contentPanel = new HorizontalPanel();    

    contentPanel.add(stepsTree);

    DecoratorPanel contentDecorator = new DecoratorPanel();
    contentDecorator.setWidget(stepContentPanel);

    contentPanel.add(contentDecorator);

    mainPanel.add(contentPanel);

    mainPanel.setWidth("100%");
    contentDecorator.setWidth("100%");
    contentPanel.setWidth("100%");

    HorizontalPanel buttonPanel = new HorizontalPanel();
    buttonPanel.add(prevButton);
    buttonPanel.add(nextButton);
    
    mainPanel.add(buttonPanel);

    treeItemToStep = new HashMap<TreeItem, WizardStep>();
    setSteps(steps);

    stepsTree.setSelectedItem(stepsTree.getItem(0));
  }

  /**
   * @return the steps
   */
  public List<WizardStep> getSteps() {
    return steps;
  }

  /**
   * @param steps
   *          the steps to set
   */
  public void setSteps(List<WizardStep> steps) {
    this.steps = steps;
    if (this.steps != null) {
      for (int i = 0; i < steps.size(); i++) {
        WizardStep wizardStep = steps.get(i);
        wizardStep.getContent().setWizardStateListener(this);
        TreeItem treeItem = stepsTree.addItem((i + 1) + ". " + wizardStep.getLabel());
        treeItems[i] = treeItem;
        treeItemToStep.put(treeItem, wizardStep);
      }
    } else {
      stepsTree.removeItems();
    }
    currentStep = 0;
  }

  /**
   * @return the contentPanel
   */
  public SimplePanel getStepContentPanel() {
    return stepContentPanel;
  }

  /**
   * @param contentPanel
   *          the contentPanel to set
   */
  public void setStepContentPanel(SimplePanel contentPanel) {
    this.stepContentPanel = contentPanel;
  }

  public void handeNotProceedable() {
    nextButton.setEnabled(false);
  }

  public void handleProceedable() {
    nextButton.setEnabled(true);
  }

  /**
   * @author josh
   *
   */
  private final class PreviousClickHandler implements ClickListener {
    public void onClick(Widget sender) {
      currentStep--;
      TreeItem item = stepsTree.getItem(currentStep);
      stepsTree.setSelectedItem(item);
    }
  }

  /**
   * @author josh
   *
   */
  private final class NextClickHandler implements ClickListener {
    public void onClick(Widget sender) {
      if(currentStep == Wizard.this.steps.size()-1) {
        // Finish
        if(finisher != null) {
          finisher.handleFinish();
        }
        return; 
      }
      currentStep++;
      TreeItem item = stepsTree.getItem(currentStep);
      stepsTree.setSelectedItem(item);
    }
  }

  private void movetoStep(int stepIndex) {
    WizardStep step = steps.get(stepIndex);
    currentStep = stepIndex;
    if (currentStep == 0) {
      prevButton.setEnabled(false);
    } else {
      prevButton.setEnabled(true);
    }
    if(currentStep == steps.size() - 1) {
      nextButton.setText("Finish");
    } else {
      nextButton.setText("Next");
    }
    nextButton.setEnabled(false);
    stepContentPanel.setWidget(step.getContent());
    step.getContent().onShow();
    stepsTree.setSelectedItem(treeItems[stepIndex], false);    
  }

  /**
   * @author josh
   * 
   */
  private final class WizardTreeListener implements TreeListener {
    public void onTreeItemSelected(TreeItem item) {
      WizardStep step = treeItemToStep.get(item);
      if (step != null) {
        if(stepContentPanel.getWidget() == step.getContent()) {
          return;
        }
        int stepIndex = steps.indexOf(step);
        if(stepIndex > currentStep + 1) {
          TreeItem old = treeItems[currentStep];
          stepsTree.setSelectedItem(old);
        }
        History.newItem(Integer.toString(stepIndex));
        movetoStep(stepIndex);
      } else {
        stepContentPanel.setWidget(new HTML("&nbsp;"));
      }
    }

    public void onTreeItemStateChanged(TreeItem item) {
    }
  }
}
