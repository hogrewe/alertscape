/**
 * 
 */
package com.alertscape.browser.upramp.firstparty.ack;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.alertscape.browser.ui.swing.AlertBrowser;
import com.alertscape.browser.upramp.model.AbstractUpRampPanel;
import com.alertscape.browser.upramp.model.UpRamp;
import com.alertscape.common.model.Alert;
import com.jgoodies.forms.factories.ButtonBarFactory;

/**
 * @author josh
 * 
 */
public class AcknowledgeAlertPanel extends AbstractUpRampPanel {
  private JButton okButton = new JButton("OK");
  private JButton cancelButton = new JButton("Cancel");
  
  private boolean submit;

  /**
   * @param upramp
   */
  public AcknowledgeAlertPanel(UpRamp upramp) {
    setUpramp(upramp);
    setContext(AlertBrowser.getCurrentContext());
  }

  @Override
  public void associateHideListener(ActionListener listener) {
    okButton.addActionListener(listener);
    cancelButton.addActionListener(listener);
  }

  @Override
  protected Map buildSubmitMap() {
    return Collections.emptyMap();
  }

  @Override
  public Dimension getBaseSize() {
    return new Dimension(300, 125);
  }

  @Override
  protected boolean initialize(Map values) {
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        submit = true;
      }
    });
    setLayout(new BorderLayout());
    JPanel buttonBar = ButtonBarFactory.buildOKCancelBar(okButton, cancelButton);
    add(buttonBar, BorderLayout.SOUTH);
    JPanel messagePanel = new JPanel();
    long numAlerts = 0;
    List<Alert> selectedAlerts = AlertBrowser.getCurrentContext().getSelectedAlerts();
    numAlerts = selectedAlerts.size();
    String alertString = numAlerts == 1 ? " alert?" : " alerts?";
    messagePanel.add(new JLabel("Are you sure you want to acknowledge " + numAlerts + alertString));
    add(messagePanel, BorderLayout.CENTER);
    return true;
  }

  @Override
  public boolean needsSubmit() {
    return submit;
  }
}
