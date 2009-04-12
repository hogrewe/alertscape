package com.alertscape.browser.upramp.firstparty.alertproperties;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.alertscape.browser.common.auth.Authentication;
import com.alertscape.browser.common.auth.AuthenticationEvent;
import com.alertscape.browser.common.auth.AuthenticationListener;
import com.alertscape.browser.ui.swing.AlertBrowser;
import com.alertscape.browser.upramp.model.AbstractUpRampAction;
import com.alertscape.browser.upramp.model.AbstractUpRampPanel;
import com.alertscape.browser.upramp.model.DummyUpRamp;
import com.alertscape.browser.upramp.model.UpRamp;

public class AlertPropertiesAction extends AbstractUpRampAction
{
	private static final long serialVersionUID = 1L;
	private ImageIcon myIcon = null;
	
	public AlertPropertiesAction() 
	{

	}
	
	@Override
	protected KeyStroke getActionAccelerator()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK);
	}

	@Override
	protected String getActionDescription()
	{
		return "Display Alert Details";
	}

	@Override
	protected Icon getActionIcon()
	{
		if (myIcon == null)
		{
			URL imageUrl = getClass().getResource("/com/alertscape/images/mini/page_text.gif");
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
		return "Alert Details";
	}

	@Override
	protected String getActionWindowTitle()
	{
		return "Alert Details";
	}

	@Override
	protected AbstractUpRampPanel getPanel()
	{
		return new AlertPropertiesPanel(AlertBrowser.getCurrentContext(), getUpramp());
	}

	@Override
	protected UpRamp getUpramp()
	{
		return new DummyUpRamp();
	}

}

