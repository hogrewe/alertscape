package com.alertscape.browser.upramp.firstparty.predefinedtag;

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

public class PredefinedTagAction extends AbstractUpRampAction
{
	private static final long serialVersionUID = 1L;
	private ImageIcon myIcon = null;	
	
	@Override
	protected KeyStroke getActionAccelerator()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK);
	}

	@Override
	protected String getActionDescription()
	{
		return "Set a tag for selected alerts";
	}

	@Override
	protected Icon getActionIcon()
	{
		if (myIcon == null)
		{
			URL imageUrl = getClass().getResource("/com/alertscape/images/mini/comment_blue.gif");
		    ImageIcon icon = new ImageIcon(imageUrl);
		    myIcon = icon;	
		}
		
		return myIcon;
	}

	@Override
	protected int getActionMnemonic()
	{
		return KeyEvent.VK_T;
	}

	@Override
	protected String getActionTitle()
	{
		return "Tag Alerts";
	}

	@Override
	protected String getActionWindowTitle()
	{
		return "Tag Alerts";
	}

	@Override
	protected AbstractUpRampPanel getPanel()
	{
		return new PredefinedTagPanel(AlertBrowser.getCurrentContext(), getUpramp());
	}

	@Override
	protected UpRamp getUpramp()
	{
		return new PredefinedTagUpRamp();
	}
}
