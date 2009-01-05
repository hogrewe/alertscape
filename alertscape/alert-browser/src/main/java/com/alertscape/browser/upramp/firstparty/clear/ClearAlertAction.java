/**
 * 
 */
package com.alertscape.browser.upramp.firstparty.clear;

import javax.swing.Icon;
import javax.swing.KeyStroke;

import com.alertscape.browser.upramp.model.AbstractUpRampAction;
import com.alertscape.browser.upramp.model.AbstractUpRampPanel;
import com.alertscape.browser.upramp.model.UpRamp;

/**
 * @author josh
 * 
 */
public class ClearAlertAction extends AbstractUpRampAction {

  private static final long serialVersionUID = 5978677927003721995L;

  @Override
  protected KeyStroke getActionAccelerator() {
    return null;
  }

  @Override
  protected String getActionDescription() {
    return "Clear Selected Alerts";
  }

  @Override
  protected Icon getActionIcon() {
    return null;
  }

  @Override
  protected int getActionMnemonic() {
    return 0;
  }

  @Override
  protected String getActionTitle() {
    return "Clear";
  }

  @Override
  protected String getActionWindowTitle() {
    return "Clear Alerts";
  }

  @Override
  protected AbstractUpRampPanel getPanel() {
    return new ClearAlertPanel(getUpramp());
  }

  @Override
  protected UpRamp getUpramp() {
    return new ClearUpramp();
  }

}
