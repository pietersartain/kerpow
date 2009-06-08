package plugins.video;

import com.kaear.common.*;
import com.kaear.interfaces.*;

public class videoMain implements plugin
{
	private static guiPlugin Gui = new videoMainGui();
	
	public videoMain()
	{
		// See if we've been run before:
		videoCommands vc = new videoCommands(null,null);
		if (!vc.checkRun()) {
			vc.createTables();
		}
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
