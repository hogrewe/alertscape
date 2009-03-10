/**
 * 
 */
package com.alertscape.wizard.client;

import com.google.gwt.user.client.ui.Composite;

/**
 * @author josh
 *
 */
public abstract class WizardContent extends Composite {
  WizardStateListener stateListener;
  
  public void setWizardStateListener(WizardStateListener listener) {
    this.stateListener = listener;
  }
  
  protected void fireNotProceedable() {
    stateListener.handeNotProceedable();
  }
  
  protected void fireProceedable() {
    stateListener.handleProceedable();
  }
  
  public abstract void onShow();
}
