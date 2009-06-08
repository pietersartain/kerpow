package plugins.music.alias;

import plugins.music.*;

import com.kaear.cli.*;
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

	private int verbosityLevel = 0;
	private int[] columnWidths = new int[4];
	private String cmd;
	private Vector args;

	public aliasCommands(String cmd, Vector args)
	{
		verbosityLevel = musicMain.verbosityLevel;
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
			
		/*} else if (cmd.equals("setMaster")) {
			
			System.out.println((String)args.get(0) + " : " + (String)args.get(1));
			
			setMaster((String)args.get(0),(String)args.get(1));
			
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
				new aliasMainGui().updateTable(showDB());
            }});
			kerpowgui.updateStatusBar("New master set.");*/
		} else {
			System.out.println("Not a recognised command, sorry.");
		}
	}

	protected int checkAlias(String master, String aliasC)
	{
		//String master.this = master.replaceAll("'","''");
		String alias = aliasC.replaceAll("'","''");
		//System.out.println(artist);
		int result = -1;
		try
		{
			ResultSet rs = kerpowObjectManager.runDB.sqlExe("SELECT * FROM alias WHERE master = '" + getMasterID(master) + "' AND alias = " + alias," ");
			rs.next();
			if (master.equals(rs.getString(2))) { result = Integer.parseInt(rs.getString(1)); }
		} 
		catch (Throwable e) { new exhandle("checkAlias failed: ", e, verbosityLevel); }
		
		return result;
	}
	
	/*
	protected void setMaster(String newMaster, String oldMaster)
	{
		plugins.music.music.musicCommands mc = new plugins.music.music.musicCommands(null,null);
		if (mc.checkArtist(newMaster) == -1) { mc.addRecord("artist",newMaster); }
		String sqlstmt = "UPDATE alias SET master = " + mc.checkArtist(newMaster) + ", alias = '" + oldMaster + "' WHERE master = " + mc.checkArtist(oldMaster);
		System.out.println(sqlstmt);
		kerpowObjectManager.runDB.sqlRun(sqlstmt, "Set master failed: ");
		
	}
	*/
	
	protected void addAlias(String master, String alias)
	{		
		String sqlstmt = "INSERT INTO alias(master,alias) VALUES(" + getMasterID(master) + ", '" + alias.replaceAll("'","''") + "')";
		if (kerpowObjectManager.runDB.sqlRun(sqlstmt,null)) { System.out.println("Updated."); }
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
		catch (Throwable e) { new exhandle("getMasterID failed: ", e, verbosityLevel); }
		
		return result;
	}
}
