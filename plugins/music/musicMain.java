package plugins.music;

import com.kaear.common.*;
import com.kaear.interfaces.*;
import plugins.music.music.*;

public class musicMain implements plugin
{
	public static musicPrefs mp = new musicPrefs();
	private static guiPlugin Gui = new musicGui();
	
	public musicMain()
	{
		// See if we've been run before:
		musicCommands mc = new musicCommands(null,null);
		if (!mc.checkRun()) {
			mc.createTables();
		}
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
