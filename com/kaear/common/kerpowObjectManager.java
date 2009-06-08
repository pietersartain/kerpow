package com.kaear.common;

import com.kaear.cli.*;
import com.kaear.gui.*;

import java.util.Vector;

public class kerpowObjectManager {

	// Objects
	public static dbase runDB = new dbase();
	public static preferences preferences = new preferences();
	public static pluginManager plugins = new pluginManager();

	public static int verbosityLevel = preferences.getVerbosity();


	public kerpowObjectManager()
	{
		initialiser ini = new initialiser();

		if (!ini.checkRun())
		{
			ini.firstRun();
		}

		//initmain();
	}
	
	private void initmain()
	{
		// Preferences
		preferences = new preferences();
		verbosityLevel = preferences.getVerbosity();
		
		// DB
		runDB = new dbase();
		
		// Plugins
		plugins = new pluginManager();
	}
}
