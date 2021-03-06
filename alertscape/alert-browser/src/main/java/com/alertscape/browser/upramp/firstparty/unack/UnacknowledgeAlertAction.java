/**
 * 
 */
package com.alertscape.browser.upramp.firstparty.unack;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.alertscape.browser.upramp.model.AbstractUpRampAction;
import com.alertscape.browser.upramp.model.AbstractUpRampPanel;
import com.alertscape.browser.upramp.model.UpRamp;

/**
 * @author josh
 * 
 */
public class UnacknowledgeAlertAction extends AbstractUpRampAction {

  private static final long serialVersionUID = 5978677927003721995L;
	private ImageIcon myIcon = null;	

  @Override
  protected KeyStroke getActionAccelerator() {
  	return KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.ALT_MASK);
  }

  @Override
  protected String getActionDescription() {
    return "Unacknowledge Selected Alerts";
  }

  @Override
  protected Icon getActionIcon() {
		if (myIcon == null)
		{
			URL imageUrl = getClass().getResource("/com/alertscape/images/mini/page_deny.gif");
		    ImageIcon icon = new ImageIcon(imageUrl);
		    myIcon = icon;	
		}
		
		return myIcon;
  }

  @Override
  protected int getActionMnemonic() {
    return 0;
  }

  @Override
  protected String getActionTitle() {
    return "Unacknowledge";
  }

  @Override
  protected String getActionWindowTitle() {
    return "Unacknowledge Alerts";
  }

  @Override
  protected AbstractUpRampPanel getPanel() {
    return new UnacknowledgeAlertPanel(getUpramp());
  }

  @Override
  protected UpRamp getUpramp() {
    return new UnacknowledgeUpramp();
  }

}
