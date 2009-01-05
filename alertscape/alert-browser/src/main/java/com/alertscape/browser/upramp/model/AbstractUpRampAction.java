package com.alertscape.browser.upramp.model;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import com.alertscape.browser.ui.swing.AlertBrowser;

public abstract class AbstractUpRampAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;
	private JFrame parentFrame;
	private UpRamp upramp;
	
	protected abstract String getActionTitle();
	protected abstract String getActionDescription();
	protected abstract int getActionMnemonic();
	protected abstract KeyStroke getActionAccelerator();
	protected abstract String getActionWindowTitle(); 
	protected abstract Icon getActionIcon();
	protected abstract AbstractUpRampPanel getPanel();
	protected abstract UpRamp getUpramp();
	
	public AbstractUpRampAction()
	{	
		super();
		putValue(NAME, getActionTitle());
		putValue(SMALL_ICON, getActionIcon());
		putValue(SHORT_DESCRIPTION, getActionDescription());
		putValue(MNEMONIC_KEY, getActionMnemonic());
		putValue(ACCELERATOR_KEY, getActionAccelerator());		
		
		// create a new upramp, and store it
		upramp = getUpramp();
		
		// check if we are currently authorized to do this, and enable accordingly
		setEnabled(upramp.isAuthorized(AlertBrowser.getCurrentContext()));
	}
	
	public void setParentFrame(JFrame parentFrame)
	{
		this.parentFrame = parentFrame;
	}

	public void actionPerformed(ActionEvent arg0)
	{
		// check if the browser is authorized to do this
		if (upramp.isAuthorized(AlertBrowser.getCurrentContext()))
		{
			final JDialog dlg = new JDialog(parentFrame);
			dlg.setTitle(getActionWindowTitle());
			dlg.setModal(true);
			dlg.toFront();
			dlg.setLayout(new BorderLayout());			
			
			// create the panel with the latest browsercontext
			final AbstractUpRampPanel panel = getPanel();
			
			dlg.setSize(panel.getBaseSize());
			dlg.setPreferredSize(panel.getBaseSize());
			
			// center within the parent
			dlg.setLocationRelativeTo(parentFrame);
			
			// add a hide listener to the panel
			ActionListener hider = new ActionListener() {
				public void actionPerformed(ActionEvent arg0)
				{
					// Hide the dialog
					dlg.setVisible(false);
					
					// check if the panel needs to be submit
					if (panel.needsSubmit())
					{
						// submit the data in the panel using the upramp already provided
						boolean submitted = panel.submit();
						
						if (submitted)
						{
							// TODO: log a message
						}
						else
						{
							// TODO: pop a window, log a message
						}
					}
				}
			};
			panel.associateHideListener(hider);
			
			// initialize the panel using the upramp
			boolean initialized = panel.initialize();
			
			// make sure initialization worked
			if (initialized)
			{		
				// add the panel to the dialog
				dlg.add(panel, BorderLayout.CENTER);
				
				// set the dialog as visible: // TODO: not sure if will be blocking method until hide...
				dlg.setVisible(true);	
			}
			else
			{
				// TODO: Pop an error message
			}						
		}
		else
		{
			// TODO: pop an error message
		}
	}
}
