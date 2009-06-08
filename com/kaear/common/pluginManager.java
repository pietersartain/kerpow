package com.kaear.common;

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
		File dir = new File("./plugins/");
		pluginList = new Vector();
		
		if (dir.isDirectory()) {
			//System.out.println(dir);

			// Build a subdir list
			String[] children = dir.list();

			// For each subdir ...
			for (int i=0; i<children.length; i++) 
			{
				// If it is a directory itself ...
				/*
				if (new File(dir, children[i]).isDirectory())
				{
					// Must be a plugin, add it to the list.
					pluginList.add(0,"plugins." + children[i].toString() + "." + children[i].toString() + "Main");
				}
				*/
				
				if (new File(dir, children[i]).isFile())
				{
				
					int linelen = children[i].toString().indexOf(".");
					String tempPlugin = children[i].toString().substring(0,linelen);
					//System.out.println(tempPlugin);
				
					// Probably a plugin, add it to the list.
					pluginList.add(0,"plugins." + tempPlugin + "." + tempPlugin + "Main");
				}
			}
		}
		
		Class c = null;
		java.lang.reflect.Constructor con = null;
		Object o = null;
		
		try	{ c = Class.forName(pluginList.get(0).toString()); }
		catch (Throwable e) { new exhandle("No class found: ", e, 1); }
		
		try { con = c.getConstructor(); }
		catch (Throwable e) { new exhandle("Failed to get constructor: ", e, 1); }
		
		try { o = con.newInstance(); }
		catch (Throwable e) { new exhandle("Failed to make a new instance: ", e, 1); }
		
		pluginObjectList.add(0,(plugin)o);

	}
	
	public Vector getPlugins()
	{
		return pluginObjectList;
	}

}
