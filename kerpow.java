import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.beans.*;

public class kerpow
{

	public static int verbosityLevel;
	private static String thePrompt;
	
	// Objects
	public static dbase runDB;
	public static preferences preferences;
	private static music music;
	private static admin admin;

	public static void main(String[] args)
	{
		initmain();
		
		if (!kerpow.checkRun())
		{
			kerpow.firstRun();
		}
		
		if (args.length == 0) {
			kerpow.cliOnly(args);
		} else if (args[0].equals("--interactive"))	{
			kerpow.interActive();
		} else {
			kerpow.cliOnly(args);
		}
	}
	
	private static void initmain()
	{
		// Preferences
		preferences = new preferences();
		verbosityLevel = preferences.getVerbosity();
		
		// DB
		runDB = new dbase();
		
		// Admin
		admin = new admin();
		
		// Music
		music = new music();
		music.setColumns(preferences.getColumns());
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
		
		runDB.deInit();
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

				if (args[0].equals("--music")) {
					music.parseCommand(clString);
				} else if (args[0].equals("--admin")) {
					admin.parseCommand(clString);
				} else {
					System.out.println("args[0] != --music");
				}
			}else{
				System.out.println("args == 0");
			}
			runDB.deInit();
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
		String[] clString = command.split("\\s");
		
		if (clString[0].equals("home")) {
			updatePrompt("default");
			//printHelpI();
		}
		
		if (thePrompt.equals(":music")) { 
			music.parseCommand(clString); 
		} else {


			if (clString[0].equals("music")) {
				updatePrompt("music");
				music.help();
			} else if (clString[0].equals("help"))	{ 
				if (clString.length > 1) {
					if (clString[1].equals("music")) { music.help(); } else
					if (clString[1].equals("downloads")) { music.help(); } else
					if (clString[1].equals("video")) { music.help(); } else
					if (clString[1].equals("software")) { music.help(); } else
					if (clString[1].equals("games")) { music.help(); } else
					if (clString[1].equals("documents")) { music.help(); } else
					if (clString[1].equals("admin")) { admin.help(); } else
					if (clString[1].equals("home")) { printHelpI(); } else
					{ printHelpI(); }
				} else { 
				printHelpI(); 
				}
			} else if (clString[0].equals("exit"))	{
				System.out.println("Farewell!"); 
			} else {
				System.out.println("Command not recognised, use \"help\" to view available commands.");
			}
		
		}
	}

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

	private static void printHelp()
	{
		String msg = "";
		msg+= "Welcome to kerpow!\n\n";
		msg+="Areas of interest:\n\n";
		msg+="	--admin\n";
		msg+="	--music\n";
		msg+="	--downloads\n";
		msg+="	--video\n";
		msg+="	--software\n";
		msg+="	--games\n";
		msg+="	--documents\n\n";
		msg+="	--<area of interest> help to show more specific commands.";
		
		System.out.println(msg);

/*		
		msg+= "Welcome to kerpow!\n\n";
		msg+="	--interactive		starts an interactive command line session.\n\n";
		msg+="	--create-tables		initialise the DB tables.\n";
		msg+="	--update		populate the tables from your hdd.\n";
		msg+="	--display		list all found music\n";
		msg+="	--help			this message\n";
		System.out.println(msg); */
	}
	
	private static void printHelpI()
	{
		String msg = "";
		/*
		msg+="Interactive options:\n\n";
		msg+="	create-tables		initialise the DB tables.\n";
		msg+="	update			populate the tables from your hdd.\n";
		msg+="	display			list all found music\n";
		msg+="	help			this message\n";
		msg+="	exit			quits kerpow\n";
		*/
		msg+="Areas of interest:\n\n";
		msg+="	home (here)\n";
		msg+="	admin\n\n";
		msg+="	music\n";
		msg+="	downloads\n";
		msg+="	video\n";
		msg+="	software\n";
		msg+="	games\n";
		msg+="	documents\n";
		msg+="\nhelp <area of interest> to show more specific commands.";
		
		System.out.println(msg);
	}
	
	private static boolean checkRun()
	{
			File runOnce = new File("run.once");
			return runOnce.exists();
	}

}
