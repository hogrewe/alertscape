package com.alertscape.browser.localramp.firstparty.preferences;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.alertscape.browser.localramp.model.AbstractLocalRampAction;
import com.alertscape.browser.localramp.model.AbstractLocalRampPanel;
import com.alertscape.browser.localramp.model.LocalRamp;
import com.alertscape.browser.localramp.model.ReadSingleObjectFromFileLocalRamp;
import com.alertscape.browser.localramp.model.WriteSingleObjectToFileLocalRamp;

public class LoadPreferencesAction extends AbstractLocalRampAction
{
	private ImageIcon myIcon = null;
	private List prefPanels;

	public LoadPreferencesAction(List <UserPreferencesPanel>prefPanels)
	{
		super();
		this.prefPanels = prefPanels;
	}	
	
	
	@Override
	protected KeyStroke getActionAccelerator()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK);
	}

	@Override
	protected String getActionDescription()
	{
		return "Load Browser Preferences from Disk";
	}

	@Override
	protected Icon getActionIcon()
	{
		if (myIcon == null)
		{
			URL imageUrl = getClass().getResource("/com/alertscape/images/mini/icon_package_open.gif");
		    ImageIcon icon = new ImageIcon(imageUrl);
		    myIcon = icon;	
		}
		
		return myIcon;
	}

	@Override
	protected int getActionMnemonic()
	{
		return KeyEvent.VK_O;
	}

	@Override
	protected String getActionTitle()
	{
		return "Load Browser Profile";
	}

	@Override
	protected String getActionWindowTitle()
	{
		return "Load Browser Profile";
	}

	@Override
	protected LocalRamp getLocalRamp()
	{
		return new ReadSingleObjectFromFileLocalRamp("amp_profile.obj");		
	}

	@Override
	protected AbstractLocalRampPanel getPanel()
	{
		return new LoadPreferencesPanel(getLocalRamp(), prefPanels);
	}

}
