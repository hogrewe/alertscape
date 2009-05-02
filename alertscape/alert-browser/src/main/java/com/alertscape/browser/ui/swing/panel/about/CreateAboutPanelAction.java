package com.alertscape.browser.ui.swing.panel.about;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.alertscape.browser.localramp.model.AbstractLocalRampPanel;
import com.alertscape.browser.ui.swing.AlertBrowser;
import com.alertscape.common.model.Alert;

public class CreateAboutPanelAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;
	private JFrame parentFrame;
	private ImageIcon myIcon;
	private ImageIcon abouticon;

	public CreateAboutPanelAction()
	{	
		super();
		putValue(NAME, getActionTitle());
		putValue(SMALL_ICON, getActionIcon());
		putValue(SHORT_DESCRIPTION, getActionDescription());
		putValue(MNEMONIC_KEY, getActionMnemonic());
		putValue(ACCELERATOR_KEY, getActionAccelerator());	
		
		URL imageUrl = getClass().getResource("/com/alertscape/images/common/as_logo2_16.png");
		abouticon = new ImageIcon(imageUrl);
	}
	
	public void setParentFrame(JFrame parentFrame)
	{
		this.parentFrame = parentFrame;
	}

	public void actionPerformed(final ActionEvent arg1)
	{	
		Thread t = new Thread() {
			public void run()
			{
					final JDialog dlg = new JDialog(parentFrame);
					dlg.setTitle(getActionWindowTitle());
					dlg.setModal(true);
					dlg.toFront();
					dlg.setLayout(new BorderLayout());
					
					// create the panel with the latest browsercontext
					final AboutPanel panel = new AboutPanel("1.x");

					dlg.setSize(panel.getBaseSize());
					dlg.setPreferredSize(panel.getBaseSize());

					// center within the parent
					dlg.setLocationRelativeTo(parentFrame);

					// add a hide listener to the panel
					ActionListener hider = new ActionListener() {
						public void actionPerformed(ActionEvent arg0)
						{
							Thread innerT = new Thread() 
							{
								public void run()
								{
									// Hide the dialog
									dlg.setVisible(false);
								}
							};
							innerT.start();
						}
					};

					panel.associateHideListener(hider);
					
  				// add the panel to the dialog
	  			dlg.add(panel, BorderLayout.CENTER);

  				// set the dialog as visible: // TODO: not sure if will be blocking
	  			// method until hide...
					dlg.setVisible(true);		
			}
		};		
		
		t.start();		
	}	
	

	protected KeyStroke getActionAccelerator()
	{
		return null;//KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK);
	}


	protected String getActionDescription()
	{
		return "About AMP";
	}


	protected Icon getActionIcon()
	{
		if (myIcon == null)
		{
			URL imageUrl = getClass().getResource("/com/alertscape/images/common/as_logo2_16.png");
		    ImageIcon icon = new ImageIcon(imageUrl);
		    myIcon = icon;	
		}
		
		return myIcon;
	}


	protected int getActionMnemonic()
	{
		//return KeyEvent.VK_L;
		return 0;
	}


	protected String getActionTitle()
	{
		return "About AMP";
	}


	protected String getActionWindowTitle()
	{
		return "About Alertscape Management Portal";
	}
}
