/*
 * Created by:  Pieter Sartain
 *
 * Licensed under the GPL:
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 *
 */

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
