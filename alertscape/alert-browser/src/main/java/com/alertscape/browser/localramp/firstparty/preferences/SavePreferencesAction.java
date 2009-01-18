package com.alertscape.browser.localramp.firstparty.preferences;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import com.alertscape.browser.localramp.model.AbstractLocalRampAction;
import com.alertscape.browser.localramp.model.AbstractLocalRampPanel;
import com.alertscape.browser.localramp.model.LocalRamp;
import com.alertscape.browser.localramp.model.WriteSingleObjectToFileLocalRamp;
import com.alertscape.browser.ui.swing.AlertBrowser;

public class SavePreferencesAction extends AbstractLocalRampAction
{
	private ImageIcon myIcon = null;
	private List prefPanels;

	public SavePreferencesAction(List <UserPreferencesPanel>prefPanels)
	{
		super();
		this.prefPanels = prefPanels;
	}
	
	@Override
	protected KeyStroke getActionAccelerator()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK);
	}

	@Override
	protected String getActionDescription()
	{
		return "Save Browser Preferences to Disk";
	}

	@Override
	protected Icon getActionIcon()
	{
		if (myIcon == null)
		{
			URL imageUrl = getClass().getResource("/com/alertscape/images/mini/icon_package_get.gif");
		    ImageIcon icon = new ImageIcon(imageUrl);
		    myIcon = icon;	
		}
		
		return myIcon;
	}

	@Override
	protected int getActionMnemonic()
	{
		return KeyEvent.VK_P;
	}

	@Override
	protected String getActionTitle()
	{
		return "Save Browser Profile";
	}

	@Override
	protected String getActionWindowTitle()
	{
		return "Save Browser Profile";
	}

	@Override
	protected LocalRamp getLocalRamp()
	{
		return new WriteSingleObjectToFileLocalRamp("amp_profile.obj");
	}

	@Override
	protected AbstractLocalRampPanel getPanel()
	{
		return new SavePreferencesPanel(getLocalRamp(), prefPanels);
	}
}
