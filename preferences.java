import java.io.*;
import java.util.ArrayList;

public class preferences
{

private ArrayList musicpath = new ArrayList(1);
private ArrayList excludepath = new ArrayList(1);
private int verbosityLevel;
private int[] columnWidths = new int[5];

	public preferences()
	{
		// Parse kerpow.prefs
		try
		{
			FileReader prefsRead = new FileReader("kerpow.prefs");
			// We won't parse a file over 1mb (1024*1024 B) in size.
			char[] thePrefs = new char[1048576];

			try
			{
				// Empty the preferences file into thePrefs
				prefsRead.read(thePrefs);
				
			} catch (Throwable e) { new exhandle("Preference reading failed: ",e,2); }
			
			// Tokenize thePrefs
			String prefString = new String(thePrefs);
			// Split by line ("\\n")
			String[] prefTokens = prefString.split("\\n");

			// Look through the tokens for lines starting with '#'
			for (int x=0; x<prefTokens.length; x++)
			{
				// Only if the line length > 0, do stuff
				if (prefTokens[x].toString().length() > 0)
				{
					// If the first character on the line is '#'
					if (prefTokens[x].charAt(0) == '#')
					{
						// Split the line by space ("\\s")
						String[] thePrefToks = prefTokens[x].split("\\s");

						if (thePrefToks[0].equals("#music"))
						{
							String mexpath = "";
							if (thePrefToks.length > 2)
							{
								for (int y=1; y<thePrefToks.length; y++)
								{
									mexpath+=thePrefToks[y];
									mexpath+=" ";
								}
								
								mexpath = mexpath.substring(0,mexpath.length()-1);
							}
							else
							{
								mexpath = thePrefToks[1];
							}
							musicpath.add(mexpath);
						}
						else if (thePrefToks[0].equals("#exclude"))
						{
							String mexpath = "";
							// If there are spaces in the path name
							if (thePrefToks.length > 2)
							{
								for (int y=1; y<thePrefToks.length; y++)
								{
									mexpath+=thePrefToks[y];
									mexpath+=" ";
								}
								
								mexpath = mexpath.substring(0,mexpath.length()-1);
							}
							else
							{
								mexpath = thePrefToks[1];
							}
							excludepath.add(mexpath);
						}
						else if (thePrefToks[0].equals("#verbosity"))
						{
							verbosityLevel = Integer.parseInt(thePrefToks[1]);
						}
						else if (thePrefToks[0].equals("#columns"))
						{
							for (int y = 1; y<=5; y++) {
							columnWidths[y-1] = Integer.parseInt(thePrefToks[y]);
							}
						}
					}
				}
			}
			
			if (verbosityLevel > 0)	{ showDebug(); } 

		} catch (Throwable e) { new exhandle("Locate file failed: ",e,2);
		}
	}
	
	private void showDebug()
	{
		System.out.println("\nVerbosity level: " + verbosityLevel);
		
		System.out.println("\nMusic paths:");
		for (int x=0; x<musicpath.size(); x++)
		{
			System.out.println(musicpath.get(x).toString());
		}
				
		System.out.println("\nExclude paths:");
		for (int x=0; x<excludepath.size(); x++)
		{
			System.out.println(excludepath.get(x).toString());
		}
		
		System.out.println("\nColumn widths:");
		for (int x=0; x<columnWidths.length; x++)
		{
			System.out.print(columnWidths[x] + "  ");
		}
		System.out.println("\n");
	}
	
	public String[] getMusicPath()
	{
		String[] mpath = new String[musicpath.size()];
		for (int x=0; x<musicpath.size(); x++)
		{
			mpath[x] = musicpath.get(x).toString();
		}
		
		return mpath;
	}
	
	public String[] getExcludePath()
	{
		String[] epath = new String[excludepath.size()];
		for (int x=0; x<excludepath.size(); x++)
		{
			epath[x] = excludepath.get(x).toString();
		}
		
		return epath;
	}
	
	public int[] getColumns()
	{
		return columnWidths;
	}
	
	public int getVerbosity()
	{
		return verbosityLevel;
	}
}
