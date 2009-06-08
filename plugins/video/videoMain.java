package plugins.video;

import com.kaear.common.*;

public class videoMain implements plugin
{

	public static int verbosityLevel = 0;
	private static guiPlugin Gui = new videoMainGui();
	//private static cliPlugin Txt = new videoText();
	
	public videoMain()
	{
		//verbosityLevel = vp.getVerbosity();
		verbosityLevel = 2;
		
		// See if we've been run before:
		videoCommands vc = new videoCommands(null,null);
		if (!vc.checkRun()) {
			vc.createTables();
		}
	}
	
	public cliPlugin getTxt()
	{
		cliPlugin Txt = null;
		return Txt;
	}

	public guiPlugin getGui()
	{
		return Gui;
	}
	
	public String getPluginName()
	{
		String myString = new String("video");
		return myString;
	}

}
