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

package plugins.minidisk;

import com.kaear.common.*;
import com.kaear.gui.*;

// Database SQL imports
import java.sql.ResultSet;

// The rest...
import java.io.File;
import java.util.Vector;
import java.util.Date;

public class minidiskCommands implements Runnable
{
	private String cmd;
	private Vector args;

	public minidiskCommands(String cmd, Vector args)
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
		if (cmd.equals("findMD")) {
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
        	public void run() {
				new minidiskGui().updateTable(findMD((String)args.get(0),(String)args.get(1)));
				kerpowgui.updateStatusBar("Search completed.");
        	}});
		} else if (cmd.equals("deleteRecord")) {
			if (deleteRecord((String)args.get(0)))
			{
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
        	    public void run() {
					new minidiskGui().updateTable(showAll());
					kerpowgui.updateStatusBar("Record deleted.");
        	    }});
			} else {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
        	    public void run() {
					kerpowgui.updateStatusBar("Delete failed.");
        	    }});
			}
		}
	}
	
	/**
	 *   Generates the video database tables, and populates any default fields.
	 */
	public void createTables()
	{
		// Make the tables:
			
		// minidisk
		kerpowObjectManager.runDB.sqlMake("CREATE TABLE minidisk(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, name varchar(128), number int, contents varchar(256))","'Video' creation failed:");
		
		// Make sure we don't do this every time
		try { new File(".status/run.minidisk").createNewFile(); }
		catch (Throwable e) { new exhandle("Cannot create file run.minidisk! ",e); }
	}

	public boolean checkRun()
	{
			File runOnce = new File(".status/run.minidisk");
			return runOnce.exists();	
	}
	
	public String showAll()
	{
		String sqlstmt = "SELECT * FROM minidisk ORDER BY number ASC";
		return sqlstmt;
	}
	
	public boolean addMD(String name, int number, String contents)
	{
		name = name.replaceAll("'","''");
		contents = contents.replaceAll("'","''");
		String sqlstmt = "INSERT INTO minidisk(name, number, contents) VALUES('" + name + "'," + number + ", '" + contents + "')";

		return kerpowObjectManager.runDB.sqlRun(sqlstmt," ");
	}
	
	public String findMD(String name, String field)
	{
		String sqlstmt = "SELECT * FROM minidisk WHERE ";
		
		if (field.equals("Number")) {
			sqlstmt+= "number = " + name;
		} else {
			sqlstmt+= field + " LIKE '%" + name + "%'";
		}
		sqlstmt+= " ORDER BY number ASC";
		return sqlstmt;
	}

	public boolean deleteRecord(String id)
	{
		return kerpowObjectManager.runDB.sqlRun("DELETE FROM minidisk WHERE id = " + id,"deleteRecord() failed: ");
	}
	
}
