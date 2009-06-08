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
