package com.alertscape.browser.localramp.model;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JPanel;

import com.alertscape.browser.model.BrowserContext;

public abstract class AbstractLocalRampPanel extends JPanel
{
	// static variables
	private static final long serialVersionUID = 1L;
	
	// member variables
	private LocalRamp localramp;
	private BrowserContext context;
	
	// this method must return the size that this panel should be displayed at
	public abstract Dimension getBaseSize();
	
	// this method should take the map of values and initialize the ui with them
	protected abstract boolean initialize(Object values);

	// this method should collect information from the ui, and put it into a map
	// for submission
	protected abstract Object buildSubmitObject();	

	// this method should return true if data needs to be submitted, false if
	// nothing needs to be submitted
	public abstract boolean needsSubmit();
	
	// this method will be called during setup, and a listener will be passed in, which will hide the window when appropriate.  This listener should be added to any buttons, menues, etc, that should trigger the window to be hidden (send, submit, cancel, etc)
	// note that this method will be called before the initialize method, in order to make sure that the listeners fire in the correct order
	public abstract void associateHideListener(ActionListener listener);
	
	// this method tells us whether this panel has valid member vars setup
	private boolean hasValidMembers()
	{
		boolean retval = false;
		if ((localramp != null) && (context != null))
		{
			retval = true;
		}
		return retval;
	}
	
	// this method initializes the panel, using the context and upramp
	public boolean initialize()
	{
		boolean retval = false;
		if (hasValidMembers())
		{
			Object values = localramp.initialize(context);
			retval = initialize(values);
		} else
		{
			// TODO: log an error message
		}
		return retval;
	}

	// this method submits the information to the server using the upramp and ui information
	public boolean submit()
	{
		boolean retval = false;
		if (hasValidMembers())
		{
			// build the submit map from the subclass
			Object values = buildSubmitObject();

			// use the upramp to submit the values to the server
			retval = localramp.submit(context, values);
		} else
		{
			// TODO: log an error message
		}
		return retval;
	}

	public void setLocalRamp(LocalRamp localramp)
	{
		this.localramp = localramp;
	}

	public LocalRamp getlocalRamp()
	{
		return localramp;
	}

	public void setContext(BrowserContext context)
	{
		this.context = context;
	}

	public BrowserContext getContext()
	{
		return context;
	}
}
