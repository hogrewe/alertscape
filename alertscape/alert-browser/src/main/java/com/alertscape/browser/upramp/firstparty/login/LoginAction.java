package com.alertscape.browser.upramp.firstparty.login;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.alertscape.browser.ui.swing.AlertBrowser;
import com.alertscape.browser.upramp.model.AbstractUpRampAction;
import com.alertscape.browser.upramp.model.AbstractUpRampPanel;
import com.alertscape.browser.upramp.model.UpRamp;

public class LoginAction extends AbstractUpRampAction
{
	private static final long serialVersionUID = 1L;
	private ImageIcon myIcon = null;
	
	@Override
	protected KeyStroke getActionAccelerator()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.ALT_MASK);
	}

	@Override
	protected String getActionDescription()
	{
		return "Log in with a different User ID";
	}

	@Override
	protected Icon getActionIcon()
	{
		if (myIcon == null)
		{
			URL imageUrl = getClass().getResource("/com/alertscape/images/mini/icon_user.gif");
		    ImageIcon icon = new ImageIcon(imageUrl);
		    myIcon = icon;	
		}
		
		return myIcon;
	}

	@Override
	protected int getActionMnemonic()
	{
		return KeyEvent.VK_U;
	}

	@Override
	protected String getActionTitle()
	{
		return "Switch User";
	}

	@Override
	protected String getActionWindowTitle()
	{
		return "Switch User";
	}

	@Override
	protected AbstractUpRampPanel getPanel()
	{
		return new LoginPanel(AlertBrowser.getCurrentContext(), getUpramp());
	}

	@Override
	protected UpRamp getUpramp()
	{
		return new LoginUpRamp();
	}

}
