package plugins.music;

import com.kaear.common.*;
import plugins.music.music.*;

public class musicMain implements plugin
{

	public static int verbosityLevel = 0;
	public static musicPrefs mp = new musicPrefs();
	private static guiPlugin Gui = new musicGui();
	private static cliPlugin Txt = new musicText();
	
	public musicMain()
	{
		verbosityLevel = mp.getVerbosity();
		
		// See if we've been run before:
		musicCommands mc = new musicCommands(null,null);
		if (!mc.checkRun()) {
			mc.createTables();
		}
	}
	
	public cliPlugin getTxt()
	{
		return Txt;
	}

	public guiPlugin getGui()
	{
		return Gui;
	}
	
	public String getPluginName()
	{
		String myString = new String("music");
		return myString;
	}

}
