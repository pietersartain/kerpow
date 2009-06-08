import java.io.File;

public class jamisii
{

//private int verbosityLevel = 0;

	public static void main(String[] args)
	{
		new jamisii().go(args);
	}
	
	private void go(String[] args)
	{
	
		if (checkRun())
		{
			int length = args.length;
			if (length == 0)
			{
				printHelp();
			} else {
				dbase runDB = new dbase();
				preferences myPrefs = new preferences();

				// music paths
				String[] musicpath = myPrefs.getMusicPath();
				String[] excludepath = myPrefs.getExcludePath();

				// verbosity
				int verbosityLevel = myPrefs.getVerbosity();
				runDB.setVerbosity(verbosityLevel);
				
				// column widths
				runDB.setColumns(myPrefs.getColumns());

				if (verbosityLevel > 0) {
					System.out.println("Music paths: ");
					for (int x = 0; x < musicpath.length; x++)
					{
						System.out.println(musicpath[x]);
					}

					System.out.println("Excluded paths: ");
					for (int x = 0; x < excludepath.length; x++)
					{
						System.out.println(excludepath[x]);
					}

					System.out.println("Verbosity level: " + verbosityLevel);
				}

				for (int index = 0; index < length; index++)
        		{
        			if (args[index].equals("--update"))
			    	{
	    				System.out.println("Updating the database ...");
						runDB.updateDB(musicpath, excludepath);	
        			}
			    	else if (args[index].equals("--display"))
			    	{
	    				System.out.println("Database contents:");
						runDB.showDB();
			    	}
    			    else if (args[index].equals("--create-tables"))
			    	{
	    				System.out.println("Creating tables ...");
						runDB.createTables();
			    	}
			    	else if (args[index].equals("--help"))
			    	{
			    		printHelp();
			    	}
        		}
				runDB.deInit();
			}
		}
		else
		{
			// We haven't been run before, so let's do some stuff ...
			// 1:  create the database & tables
			dbase runDB = new dbase();
			runDB.setVerbosity(2);
			//System.out.println("Verbosity: " + runDB.getVerbosity());
			System.out.println("Creating & populating tables ...");
			runDB.createTables();
			runDB.deInit();
			System.out.println("You must edit your preferences (jamisii.prefs) file to");
			System.out.println("point to your music directory.  Then run --update to");
			System.out.println("enter that information into the database!");
			
			// 2:  create the run.once file, so we don't do this every time
			try { new File("run.once").createNewFile(); }
			catch (Throwable e) { System.out.println("Cannot create file! (" + e + ")"); }
		}
	}

	private void printHelp()
	{
		String msg = "";
		msg+= "Welcome to Jamisii!\n\n";
		msg+="	--create-tables		initialise the DB tables.  Must be run once.\n";
		msg+="	--update		populate the tables from your hdd.\n";
		msg+="	--display		list all found music\n";
		msg+="	--help			this message\n";
		System.out.println(msg);
	}
	
	private boolean checkRun()
	{
			File runOnce = new File("run.once");
			return runOnce.exists();
	}
}
