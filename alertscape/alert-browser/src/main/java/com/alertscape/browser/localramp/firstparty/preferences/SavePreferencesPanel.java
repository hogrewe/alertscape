package com.alertscape.browser.localramp.firstparty.preferences;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.alertscape.browser.localramp.model.AbstractLocalRampPanel;
import com.alertscape.browser.localramp.model.LocalRamp;
import com.alertscape.browser.ui.swing.AlertBrowser;

import com.jgoodies.forms.factories.ButtonBarFactory;

public class SavePreferencesPanel extends AbstractLocalRampPanel {
  private JButton okButton = new JButton("OK");
  private JButton cancelButton = new JButton("Cancel");
  private List <UserPreferencesPanel>prefPanels;
  
  private boolean submit;

  /**
   * @param upramp
   */
  public SavePreferencesPanel(LocalRamp localramp, List <UserPreferencesPanel>prefPanels) {
    setLocalRamp(localramp);
    setContext(AlertBrowser.getCurrentContext());
    this.prefPanels = prefPanels;
  }

  @Override
  public void associateHideListener(ActionListener listener) {
    okButton.addActionListener(listener);
    cancelButton.addActionListener(listener);
  }

  @Override
  protected Map buildSubmitObject() 
  {
  	Map retval = new HashMap();
  	// build out all of the preferences from the preference panels
  	Iterator <UserPreferencesPanel>it = prefPanels.iterator();
  	while (it.hasNext())
  	{
  		UserPreferencesPanel panel = it.next();
  		Map prefs = panel.getUserPreferences();
  		retval.putAll(prefs);
  	}    
  	
  	return retval;  	
  }

  @Override
  public Dimension getBaseSize() {
    return new Dimension(300, 125);
  }

  @Override
  protected boolean initialize(Object val) {
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        submit = true;
      }
    });
    setLayout(new BorderLayout());
    JPanel buttonBar = ButtonBarFactory.buildOKCancelBar(okButton, cancelButton);
    add(buttonBar, BorderLayout.SOUTH);
    JPanel messagePanel = new JPanel();
    messagePanel.add(new JLabel("Save these preferences as default?"));
    add(messagePanel, BorderLayout.CENTER);
    return true;
  }

  @Override
  public boolean needsSubmit() {
    return submit;
  }
}