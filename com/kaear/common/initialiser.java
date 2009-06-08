package com.kaear.common;

import java.io.File;


public class initialiser
{
	public initialiser()
	{
		if (!checkRun())
		{
			firstRun();
		}
	}

	private void firstRun()
	{
		// We haven't been run before, so let's do some stuff ...
		
		// provide some information for the user to read
		System.out.println("kerpow has detected it has not been run before.");
		System.out.println("Creating & populating tables ...\n");
		
		// Do the shiznit.
		makeRun();
		
	}

	private boolean checkRun()
	{
			File runOnce = new File(".status/run.once");
			return runOnce.exists();
			
	}
	
	private void makeRun()
	{
		try { 
			new File(".status").mkdir();
			new File(".status/run.once").createNewFile();
		}
		catch (Throwable e) { new exhandle("Cannot create file run.once! ",e,2); }
	}
}
