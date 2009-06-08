package plugins.minidisk;

import com.kaear.common.*;
import com.kaear.interfaces.*;

public class minidiskMain implements plugin
{
	private static guiPlugin Gui = new minidiskGui();
	
	public minidiskMain()
	{
		// See if we've been run before:
		minidiskCommands vc = new minidiskCommands(null,null);
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
		String myString = new String("minidisk");
		return myString;
	}

}
