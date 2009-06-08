package com.kaear.common;

import java.io.File;


public class initialiser
{
	public initialiser()
	{
	}

	protected void firstRun()
	{
		// We haven't been run before, so let's do some stuff ...
		
		// 1:  create the database
		//runDB = new dbase();
		//music myMusic = new music();
		
		// 2:  assume a full verbosity level until otherwise stated
		kerpowObjectManager.runDB.setVerbosity(2);
		
		// 3:  provide some information for the user to read
		System.out.println("kerpow has detected it has not been run before.");
		System.out.println("Creating & populating tables ...\n");
		
		// 4:  generate & populate the tables for each plugin in the plugins directory.
		//music.createTables();
		
		//kerpowObjectManager.plugins
		
		// 5:  deinitialise the DB connections (causing record update)
		kerpowObjectManager.runDB.deInit();
		
		// 6:  provide some debriefing information for the user.
		System.out.println("You must edit your preferences (kerpow.prefs) file to");
		System.out.println("point to your music directory.  Then run --update to");
		System.out.println("enter that information into the database!\n");
		
		// 2:  create the run.once file, so we don't do this every time
		try { new File("run.once").createNewFile(); }
		catch (Throwable e) { new exhandle("Cannot create file! ",e,2); }
	}

	protected boolean checkRun()
	{
			File runOnce = new File("run.once");
			return runOnce.exists();
	}
}
