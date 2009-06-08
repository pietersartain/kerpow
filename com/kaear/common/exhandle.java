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

public class exhandle
{

	private int verbosityLevel = kerpowObjectManager.verbosityLevel;

	public exhandle(String message, Throwable e)
	{
		int flag = 0;
		String eX = "";
		if (e != null) { eX = e.toString(); }

		// Print everything that gets sent here.
		if (verbosityLevel > 1)
		{
			System.out.println(message + eX);
		}
		// Print only things that aren't usual ...
		else if (verbosityLevel == 1) {
			if (e != null) {
				//Usual list goes here
				if (e.toString().equals("SQL Exception: Invalid cursor state - no current row.")) { flag = 0; }
				else if (eX.equals("null")) { flag = 0; }
				else { flag = 1; }
			}
			if (flag == 1) { System.out.println(message + eX); }
		}
	}
}
