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

import java.util.Vector;
import java.lang.Class.*;
import java.lang.Object.*;
import java.io.File;
import java.lang.String;

public class pluginManager
{
	private static Vector pluginList;
	public static Vector pluginObjectList;

	public pluginManager()
	{
		pluginObjectList = new Vector();
		dynamicPlugin();
		pluginList = null;
	}

	private static void dynamicPlugin()
	{
		// Build a list of plugins in the /plugins/ directory:
		File dir = new File("plugins/");
		pluginList = new Vector();
		
		if (dir.isDirectory()) {
			//System.out.println(dir);

			// Build a subdir list
			String[] children = dir.list();

			// For each subdir ...
			for (int i=0; i<children.length; i++) 
			{
				// if it's a file ...
				if (new File(dir, children[i]).isFile())
				{

					// if it ends with ".jar" ...
					if (children[i].toString().endsWith(".jar"))
					{
						int linelen = children[i].toString().indexOf(".");
						String tempPlugin = children[i].toString().substring(0,linelen);
						
						// Get the existing classpath:
						//String curCP = System.getProperty("java.class.path");
						
						//System.out.println(curCP);
						
						// Add it to the classpath:
						//System.setProperty("java.class.path",curCP + ":plugins/" + tempPlugin + ".jar");
						
						//System.out.println(System.getProperty("java.class.path"));
						
						// Probably a plugin, add it to the list.
						pluginList.add(0,"plugins." + tempPlugin + "." + tempPlugin + "Main");
					}
				}
			}
		}
		
		// For every plugin in the list, get an object from it.
		for (int x=0; x < pluginList.size(); x++)
		{
			Class c = null;
			java.lang.reflect.Constructor con = null;
			Object o = null;

			try	{ c = Class.forName(pluginList.get(x).toString()); }
			catch (Throwable e) { new exhandle("No class found: ", e); }

			try { con = c.getConstructor(); }
			catch (Throwable e) { new exhandle("Failed to get constructor: ", e); }

			try { o = con.newInstance(); }
			catch (Throwable e) { new exhandle("Failed to make a new instance: ", e); }

			pluginObjectList.add(0,(plugin)o);
		}
	}
	
	public Vector getPlugins()
	{
		return pluginObjectList;
	}

}
