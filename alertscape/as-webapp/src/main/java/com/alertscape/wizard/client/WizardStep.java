/**
 * 
 */
package com.alertscape.wizard.client;


/**
 * @author josh
 *
 */
public class WizardStep {
  private String label;
  private WizardContent content;
  

  /**
   * @param label
   * @param content
   */
  public WizardStep(String label, WizardContent content) {
    super();
    this.label = label;
    this.content = content;
  }
  
  /**
   * @return the label
   */
  public String getLabel() {
    return label;
  }

  /**
   * @param label
   *          the label to set
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * @return the content
   */
  public WizardContent getContent() {
    return content;
  }

  /**
   * @param content
   *          the content to set
   */
  public void setContent(WizardContent content) {
    this.content = content;
  }
}
