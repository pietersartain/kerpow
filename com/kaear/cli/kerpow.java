package com.kaear.cli;

import com.kaear.common.*;
import com.kaear.gui.*;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;
import java.beans.*;
import java.lang.Thread;

public class kerpow
{

	public static int verbosityLevel;
	private static String thePrompt;
	public static kerpowObjectManager kerpowObjectManager;
	
	public kerpow()
	{
		kerpowObjectManager = new kerpowObjectManager();
	}
		
	public static void main(String[] args)
	{
		
		kerpow kerpow = new kerpow();
		
		if (args.length == 0) {
			kerpow.cliOnly(args);
		} else if (args[0].equals("--interactive"))	{
			kerpow.interActive();
		} else {
			kerpow.cliOnly(args);
		}
		
	}
	
	private static void interActive()
	{
		String command = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Welcome to kerpow.  Type \"help\" to view the interactive commands.");
		thePrompt = "";

		while (!command.equals("exit"))
		{
			//Generate the prompt
			System.out.print("kerpow" + thePrompt + "> ");

			try { command = br.readLine(); } catch (Throwable e) { new exhandle("Unable to read the command line",e,2); }
			
			parseInteractiveCommand(command);
			
		}
		kerpowObjectManager.runDB.deInit();
	}

	private static void cliOnly(String[] args)
	{
	
		int length = args.length;
		if (length == 0)
		{
			printHelp();
		} else {
			// run command
			if (args.length > 0) {

				String[] clString = new String[args.length - 1];
				for (int x = 1; x < args.length; x++)
				{
					clString[x-1] = args[x];
				}
				
				
				
				// Get the plugin list
				Vector pluginList = kerpowObjectManager.plugins.getPlugins();

				// For each plugin ...
				for (int x=0; pluginList.size() > x; x++)
				{
					plugin curPlugin = (plugin)pluginList.get(x);
					
					if (args[0].equals("--" + curPlugin.getPluginName())) {
						curPlugin.getTxt().parseCliCommand(clString);
					}
				}
			}else{
				System.out.println("args == 0");
			}
			kerpowObjectManager.runDB.deInit();
		}
	}
	
	private static void updatePrompt(String prompt)
	{
		if (prompt.equals("default")) {
			thePrompt = "";
		} else {
			thePrompt = ":" + prompt;
		}
	}
		
	
	private static void parseInteractiveCommand(String command)
	{
		// Tokenize the command string coming in with spaces.
		String[] clString = command.split("\\s");
		
		// If the first word is "home", set the prompt to it's default value.
		if (clString[0].equals("home")) {
			updatePrompt("default");
			//printHelpI();
		}

		// Get the plugin list
		Vector pluginList = kerpowObjectManager.plugins.getPlugins();

		// For each plugin ...
		for (int x=0; pluginList.size() > x; x++)
		{
			plugin curPlugin = (plugin)pluginList.get(x);
			cliPlugin cliPlugin = (cliPlugin)curPlugin.getTxt();

			// If the prompt is a plugin name
			if (thePrompt.equals(":" + curPlugin.getPluginName())) {
				// Send the whole command to the plugin's command parser.
				cliPlugin.parseCommand(clString);

			// Otherwise ...
			} else{
			
				// If the first word is a plugin name
				if (clString[0].equals(curPlugin.getPluginName())) {
					// Update the prompt to the plugin name
					updatePrompt(curPlugin.getPluginName());
					// Show plugin's help.
					//(cliPlugin)
					cliPlugin.help();
			
				// Else if the first word is "help"
				} else if (clString[0].equals("help")) {

					// If there is more than one word in the command string
					if (clString.length > 1) {

						// If the second word is a plugin name
						if (clString[1].equals(curPlugin.getPluginName())) {
							// Show plugin's help
							//(cliPlugin)
							cliPlugin.help();
						} else {
							printHelpI();
						}
					
					} else {
						printHelpI();
					}
				} else {
					printHelpI();
				}
			}
		}

		if (clString[0].equals("exit"))	{
			System.out.println("Farewell!"); 
		}
	}
/*
	private static void firstRun()
	{
		// We haven't been run before, so let's do some stuff ...
		
		// 1:  create the database
		//runDB = new dbase();
		//music myMusic = new music();
		
		// 2:  assume a full verbosity level until otherwise stated
		runDB.setVerbosity(2);
		
		// 3:  provide some information for the user to read
		System.out.println("kerpow has detected it has not been run before.");
		System.out.println("Creating & populating tables ...\n");
		
		// 4:  generate & populate the tables from the dbase file
		music.createTables();
		
		// 5:  deinitialise the DB connections (causing record update)
		runDB.deInit();
		
		// 6:  provide some debriefing information for the user.
		System.out.println("You must edit your preferences (kerpow.prefs) file to");
		System.out.println("point to your music directory.  Then run --update to");
		System.out.println("enter that information into the database!\n");
		
		// 2:  create the run.once file, so we don't do this every time
		try { new File("run.once").createNewFile(); }
		catch (Throwable e) { new exhandle("Cannot create file! ",e,2); }
	}
*/
	private static void printHelp()
	{
		String msg = "";
		msg+= "Welcome to kerpow!\n\n";
		msg+="Plugins available:\n\n";
		
		// Get the plugin list
		Vector pluginList = kerpowObjectManager.plugins.getPlugins();

		// For each plugin ...
		for (int x=0; pluginList.size() > x; x++)
		{
			plugin curPlugin = (plugin)pluginList.get(x);
			msg+="	--" + curPlugin.getPluginName() + "\n";
		}
/*
		msg+="	--admin\n";
		msg+="	--music\n";
		msg+="	--downloads\n";
		msg+="	--video\n";
		msg+="	--software\n";
		msg+="	--games\n";
		msg+="	--documents\n\n";
*/
		msg+="\n";
		msg+="	--<plugin> help to show more specific commands.";
		
		System.out.println(msg);

	}
	
	private static void printHelpI()
	{
		String msg = "";
		
		msg+="Interactive commands:\n\n";
		/*
		msg+="	create-tables		initialise the DB tables.\n";
		msg+="	update			populate the tables from your hdd.\n";
		msg+="	display			list all found music\n";
		msg+="	help			this message\n";
		msg+="	exit			quits kerpow\n";
		*/
		msg+="	home (here)\n\n";
		msg+="Plugins available:\n\n";
		
		// Get the plugin list
		Vector pluginList = kerpowObjectManager.plugins.getPlugins();

		// For each plugin ...
		for (int x=0; pluginList.size() > x; x++)
		{
			plugin curPlugin = (plugin)pluginList.get(x);
			msg+="	" + curPlugin.getPluginName() + "\n";
		}
		
/*		msg+="	admin\n\n";
		msg+="	music\n";
		msg+="	downloads\n";
		msg+="	video\n";
		msg+="	software\n";
		msg+="	games\n";
		msg+="	documents\n";
*/
		msg+="\nhelp <plugin> to show more specific commands.";
		
		System.out.println(msg);
	}
/*	
	private static boolean checkRun()
	{
			File runOnce = new File("run.once");
			return runOnce.exists();
	}
*/
}
