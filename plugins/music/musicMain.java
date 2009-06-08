package plugins.music;

import com.kaear.common.*;

public class musicMain implements plugin
{

	public static int verbosityLevel = 0;
	public static musicPrefs mp = new musicPrefs();
	private static guiPlugin Gui = new musicGui();
	private static cliPlugin Txt = new musicText();
	
	public musicMain()
	{
		//mp = new musicPrefs();
		verbosityLevel = mp.getVerbosity();
		
	}
	
	public cliPlugin getTxt()
	{
		//cliPlugin Txt = new musicText();
		return Txt;
	}

	public guiPlugin getGui()
	{
		//guiPlugin Gui = new musicGui();
		return Gui;
	}
	
	public String getPluginName()
	{
		String myString = new String("music");
		return myString;
	}

}
