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

package com.kaear.common;

import com.kaear.interfaces.*;
import java.io.File;


public class initialiser
{
	public initialiser()
	{
		if (!checkRun())
		{
			firstRun();
		}
	}

	private void firstRun()
	{
		// We haven't been run before, so let's do some stuff ...
		
		// provide some information for the user to read
		new exhandle("kerpow has detected it has not been run before.\nCreating & populating tables ...\n",null);

		// Do the shiznit.
		makeRun();
		
	}

	private boolean checkRun()
	{
			File runOnce = new File(".status/run.once");
			return runOnce.exists();
			
	}
	
	private void makeRun()
	{
		try { 
			new File(".status").mkdir();
			new File(".status/run.once").createNewFile();
		}
		catch (Throwable e) { new exhandle("Cannot create file run.once! ",e); }
	}
}
