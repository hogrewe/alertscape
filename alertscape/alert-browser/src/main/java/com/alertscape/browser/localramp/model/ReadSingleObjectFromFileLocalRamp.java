package com.alertscape.browser.localramp.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.alertscape.browser.model.BrowserContext;
import com.alertscape.browser.ui.swing.AlertBrowser;
import com.alertscape.common.logging.ASLogger;

public class ReadSingleObjectFromFileLocalRamp implements LocalRamp
{
	private String filename;
	
	public ReadSingleObjectFromFileLocalRamp(String filename)
	{
		super();
		this.setFilename(filename);
	}
	
	public Object initialize(BrowserContext context)
	{
		Object retval = null;
		
		try
		{
			FileInputStream fis = new FileInputStream(getFilename());
			ObjectInputStream ois = new ObjectInputStream(fis);

			Object val = ois.readObject();
			ois.close();
			retval = val;
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		return retval;
	}

	public boolean submit(BrowserContext context, Object val)
	{
		boolean retval = false;
//	
//		try
//		{
//			FileOutputStream fos = new FileOutputStream(getFilename());
//			ObjectOutputStream oos = new ObjectOutputStream(fos);
//
//			oos.writeObject(val);
//			oos.close();
//			
//			ASLogger.getLogger(AlertBrowser.class).debug("saved object to " + getFilename());			
//		}
//		catch (Throwable t)
//		{
//			ASLogger.getLogger(AlertBrowser.class).error("failed to save object to " + getFilename(), t);
//		}
		return retval;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public String getFilename()
	{
		return filename;
	}
}