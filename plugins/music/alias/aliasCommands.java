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

package plugins.music.alias;

import plugins.music.*;

import com.kaear.common.*;
import com.kaear.gui.*;

// Database SQL imports
import java.sql.ResultSet;

// The rest...
import java.io.File;
import java.util.Vector;
import java.util.Date;

public class aliasCommands implements Runnable
{

	private int[] columnWidths = new int[4];
	private String cmd;
	private Vector args;

	public aliasCommands(String cmd, Vector args)
	{
		this.args = new Vector();
		this.args = args;
		this.cmd = cmd;

		if ((args != null) || (cmd != null)) {
	  		new Thread (this, "Commands").start();
		}
	}

	public void run()
	{
		decodeCommand(cmd);
	}

	/**
	 *	The bit that decodes the commands to run in a thread!  Cool beans!
	 */
	private void decodeCommand(String cmd)
	{
		if (cmd.equals("showDB")) {
			showDB();
		} else if (cmd.equals("checkAlias")) {
			checkAlias((String)args.get(0), (String)args.get(1));
		} else if (cmd.equals("runSQL")) {
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
				new aliasMainGui().updateTable((String)args.get(0));
            }});

			kerpowgui.updateStatusBar("SQL query completed.");
		} else if (cmd.equals("deleteAlias")) {
			kerpowObjectManager.runDB.sqlRun("DELETE FROM alias WHERE id = " + (String)args.get(0), "Delete from 'alias' failed: ");
			kerpowgui.updateStatusBar("Record deleted.");
		} else {
			new exhandle("Not a recognised command, sorry.",null);
		}
	}

	protected int checkAlias(String master, String aliasC)
	{
		String alias = aliasC.replaceAll("'","''");
		int result = -1;
		try
		{
			ResultSet rs = kerpowObjectManager.runDB.sqlExe("SELECT * FROM alias WHERE master = '" + getMasterID(master) + "' AND alias = " + alias," ");
			rs.next();
			if (master.equals(rs.getString(2))) { result = Integer.parseInt(rs.getString(1)); }
		} 
		catch (Throwable e) { new exhandle("checkAlias failed: ", e); }
		
		return result;
	}
	
	protected boolean addAlias(String master, String alias)
	{		
		String sqlstmt = "INSERT INTO alias(master,alias) VALUES(" + getMasterID(master) + ", '" + alias.replaceAll("'","''") + "')";
		return kerpowObjectManager.runDB.sqlRun(sqlstmt,null);
	}

	/**
	 *   Show the whole alias database; in SQL format.
	 */
	protected String showDB()
	{
		String sqlstmt = "SELECT alias.id, artist.name, alias.alias FROM alias JOIN artist ON alias.master = artist.id ORDER BY artist.name DESC";
		return sqlstmt;
	}
	
	/**
	 *   Checks if an artist exists in the table "artist".  Returns the artist ID or -1 if it doesn't exist.
	 */
	protected int getMasterID(String artistInfo)
	{
		String artist = artistInfo.replaceAll("'","''");
		//System.out.println(artist);
		int result = -1;
		try
		{
			ResultSet rs = kerpowObjectManager.runDB.sqlExe("SELECT * FROM artist WHERE name = '" + artist + "'"," ");
			rs.next();
			if (artist.equals(rs.getString(2))) { result = Integer.parseInt(rs.getString(1)); }
		} 
		catch (Throwable e) { new exhandle("getMasterID failed: ", e); }
		
		return result;
	}
}
