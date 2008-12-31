package com.alertscape.browser.upramp.firstparty.mail;

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

public class AlertMailAction extends AbstractUpRampAction
{
	private static final long serialVersionUID = 1L;
	private ImageIcon myIcon = null;

	@Override
	protected String getActionDescription()
	{
		return "Email selected alerts to a recipient";
	}

	@Override
	protected int getActionMnemonic()
	{
		return KeyEvent.VK_E;
	}

	@Override
	protected String getActionWindowTitle()
	{
		return "Email Alerts";
	}

	@Override
	protected AbstractUpRampPanel getPanel()
	{
		return new AlertMailPanel(AlertBrowser.getCurrentContext(), getUpramp());
	}

	@Override
	protected UpRamp getUpramp()
	{
		return new AlertUpRamp();
	}

	@Override
	protected KeyStroke getActionAccelerator()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK);
	}

	@Override
	protected Icon getActionIcon()
	{
		if (myIcon == null)
		{
			URL imageUrl = getClass().getResource("/com/alertscape/images/mini/icon_email.gif");
		    ImageIcon icon = new ImageIcon(imageUrl);
		    myIcon = icon;	
		}
		
		return myIcon;
	}

	@Override
	protected String getActionTitle()
	{
		return "Email Alerts";
	}
}
