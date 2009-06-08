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

import com.kaear.gui.*;
import com.kaear.interfaces.*;

import java.util.Vector;

public class kerpowObjectManager {

	// Objects
	public static dbase runDB; // = new database();
	private static initialiser ini = new initialiser();
	public static preferences preferences = new preferences();
	public static pluginManager plugins = new pluginManager();

	public static int verbosityLevel = preferences.getVerbosity();


	public kerpowObjectManager(String whichDB)
	{
		//if (whichDB.equals("local")) {
			runDB = new database();
			kerpowgui.updateStatusBar("Using local database.");
		//} else {
		//	runDB = new database();
		//	kerpowgui.updateStatusBar("Using remote database.");
		//}
	}
}
