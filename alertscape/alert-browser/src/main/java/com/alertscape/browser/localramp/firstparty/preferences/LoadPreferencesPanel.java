package com.alertscape.browser.localramp.firstparty.preferences;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class LoadPreferencesPanel extends AbstractLocalRampPanel {
  private JButton okButton = new JButton("OK");
  private JButton cancelButton = new JButton("Cancel");
  private List <UserPreferencesPanel>prefPanels;
  
  private boolean submit;

  /**
   * @param upramp
   */
  public LoadPreferencesPanel(LocalRamp localramp, List <UserPreferencesPanel>prefPanels) {
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
  	// TODO: yech - this is the only way I can think of to shoehorn this into the localramp framework, need to think of a better way
    // should just be returning a set of values here, not doing the actual submit work....
  	
  	Iterator <UserPreferencesPanel>it = prefPanels.iterator();
  	while (it.hasNext())
  	{
  		UserPreferencesPanel panel = it.next();
  		panel.setUserPreferences(prefValues);
  	}
  	  	
  	return prefValues;
  }
  
  @Override
  public Dimension getBaseSize() {
    return new Dimension(300, 125);
  }

  private Map prefValues;
  
  @Override
  protected boolean initialize(Object val) 
  {
    okButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e) 
      {
        submit = true;
      }
    });
    
    setLayout(new BorderLayout());
    JPanel buttonBar = ButtonBarFactory.buildOKCancelBar(okButton, cancelButton);
    add(buttonBar, BorderLayout.SOUTH);
    JPanel messagePanel = new JPanel();
    messagePanel.add(new JLabel("Load default preferences?"));
    add(messagePanel, BorderLayout.CENTER);
    
    // the object here is the set of values to push to all of the prefpanels if they hit ok....
    prefValues = (Map)val;
    
    return true;
  }

  @Override
  public boolean needsSubmit() {
    return submit;
  }
}