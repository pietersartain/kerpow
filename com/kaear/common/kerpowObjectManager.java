package com.kaear.common;

import com.kaear.cli.*;
import com.kaear.gui.*;

import java.util.Vector;

public class kerpowObjectManager {

	// Objects
	public static dbase runDB = new dbase();
	private static initialiser ini = new initialiser();
	public static preferences preferences = new preferences();
	public static pluginManager plugins = new pluginManager();

	public static int verbosityLevel = preferences.getVerbosity();


	public kerpowObjectManager()
	{
	}
}
