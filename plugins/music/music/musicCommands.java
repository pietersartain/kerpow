package plugins.music.music;

import com.kaear.cli.*;
import com.kaear.common.*;
import com.kaear.gui.*;
import plugins.music.*;

// Database SQL imports
import java.sql.ResultSet;

// The rest...
import java.io.File;
import java.util.Vector;
import java.util.Date;

public class musicCommands implements Runnable
{

	private int verbosityLevel = 0;
	private int[] columnWidths = new int[4];
	private String cmd;
	private Vector args;

	public musicCommands(String cmd, Vector args)
	{
		verbosityLevel = musicMain.verbosityLevel;
		this.args = new Vector();
		this.args = args;
		this.cmd = cmd;

		if ((args != null) || (cmd != null)) {
	  		new Thread (this, "Commands").start();
		}
	}

/*
	private void countThreads()
	{
		  Thread [] threads = new Thread [Thread.activeCount ()];
    	  int n = Thread.enumerate (threads);

		  System.out.println(n);
    	  for (int i = 0; i < n; i++)
        	   System.out.println (threads [i].toString ());
	}
	
	private void checkThreads()
	{
		Thread[] threads = new Thread[Thread.activeCount ()];
		int n = Thread.enumerate (threads);

		for (int i = 0; i < n; i++)
		{
			System.out.println (threads[i].toString ());
			if (threads[i].getName().equals("Commands"))
			{
			/*
				try { threads[i].join(); }
				catch (Throwable e) { 
					new exhandle("Unable to join the \"Commands\" thread: ",e,2); 
				}
			*/
			/*
			System.out.println("Yay! I'm alive!");
			}
		}
	}
*/	
	public void run()
	{
		decodeCommand(cmd);
	}

	/**
	 *	The bit that decodes the commands to run in a thread!  Cool beans!
	 */
	private void decodeCommand(String cmd)
	{
		if (cmd.equals("updateDB")) {
			updateDB((String[])args.get(0), (String[])args.get(1));

			javax.swing.SwingUtilities.invokeLater(new Runnable() {
             public void run() {
				new musicMainGui().updateTable(showDB());
             }});
		
		kerpowgui.updateStatusBar("Update completed.");				


		} else if (cmd.equals("showDB")) {
			showDB();
		} else if (cmd.equals("searchDB")) {

			javax.swing.SwingUtilities.invokeLater(new Runnable() {
             public void run() {
				new musicMainGui().updateTable(searchDB((String)args.get(0),(String)args.get(1)));
             }});

		kerpowgui.updateStatusBar("Search finished.");				
			 
			//searchDB((String)args.get(0),(String)args.get(1));
		} else if (cmd.equals("editRecord")) {
			editRecord((String)args.get(0),(String)args.get(1),(String)args.get(2));
		} else if (cmd.equals("editMusic")) {
			editMusic((String)args.get(0),(String)args.get(1),(String)args.get(2),(String)args.get(3),(String)args.get(4));
		} else if (cmd.equals("addRecord")) {
			addRecord((String)args.get(0),(String)args.get(1));
		} else if (cmd.equals("addMusic")) {
			addMusic((String)args.get(0),(String)args.get(1),(String)args.get(2),(String)args.get(3));
			
		} else if (cmd.equals("runSQL")) {
		
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
				new musicMainGui().updateTable((String)args.get(0));
            }});

			kerpowgui.updateStatusBar("SQL query completed.");

		} else if (cmd.equals("deleteMusic")) {

			kerpowObjectManager.runDB.sqlRun("DELETE FROM music WHERE id = " + args.get(0), "Delete from 'music' failed: ");

			kerpowgui.updateStatusBar("Record deleted.");
			
		
		} else if (cmd.equals("cleanMusic")) {
			
			kerpowgui.updateStatusBar(cleanMusic() + " artist(s) removed.");
			
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
				new musicMainGui().updateTable(showDB());
            }});

			
			
		/*} else if (cmd.equals("checkAlbum")) {
			addMusic((String)args.get(0),(String)args.get(1),(String)args.get(2),(String)args.get(3));
		} else if (cmd.equals("checkMusic")) {
			addMusic((String)args.get(0),(String)args.get(1),(String)args.get(2),(String)args.get(3));
		*/
		} else {
			System.out.println("Not a recognised command, sorry.");
		}
	}
	
	/**
	 *   Generates the music database tables, and populates any default fields.
	 */
	public void createTables()
	{
		// Make the tables:
			
		// Artist
		kerpowObjectManager.runDB.sqlMake("create table artist(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, name varchar(128))","'Artist' creation failed:");
		
		// Alias
		kerpowObjectManager.runDB.sqlMake("create table alias(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, master int, alias varchar(128))","'Alias' creation failed:");

		// Album	
		kerpowObjectManager.runDB.sqlMake("create table album(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, name varchar(128))","'Album' creation failed:");
		
		// Music
		kerpowObjectManager.runDB.sqlMake("create table music(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, artist int, album int, format int, disc int)","'Music' creation failed:");
			
		// Format
		kerpowObjectManager.runDB.sqlMake("create table format(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, name varchar(128))","'Format' creation failed:");
		
		// Populate the format table
		kerpowObjectManager.runDB.sqlRun("insert into format(name) values('MP3')","\"format - MP3\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into format(name) values('CD')","\"format - CD\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into format(name) values('CD Original')","\"format - CD Original\" population failed:");
		
		// Make sure we don't do this every time
		try { new File(".status/run.music").createNewFile(); }
		catch (Throwable e) { new exhandle("Cannot create file run.music! ",e,2); }
	}
	
	public boolean checkRun()
	{
			File runOnce = new File(".status/run.music");
			return runOnce.exists();	
	}
	 
	/**
	 *   Updates the music database from the details in the prefs file.
	 */
	protected void updateDB(String[] mpath, String[] epath/*, String format*/)
	{
				System.out.println("Now updating the music database ...");
		String[] musicpath = mpath;
		for (int x = 0; x < musicpath.length; x++)
		{
			File dir = new File(musicpath[x]);
			
			//if (format.equals("artist / album")
			updateArtist(dir, epath);
		}
	}
	
	/**
	 *   A searchable display function that returns an SQL statement.
	 */
	protected String searchDB(String type, String value)
	{
		if (type.equals("Artist"))
		{
			return searchAlias(value);
		}
		else if (type.equals("Disc"))
		{
			return searchDisc(value);
		} else {
			int intVal = -1;
			try { intVal = Integer.parseInt(value,10);
			} catch (Throwable e) { new exhandle("Value is not a number, therefore must be a string: ", e, verbosityLevel); }
			String sqlstmt = "SELECT music.id, artist.name, album.name, format.name, music.disc FROM music JOIN artist ON music.artist = artist.id JOIN format ON music.format = format.id JOIN album ON music.album = album.id";
			if (intVal == -1) {
				sqlstmt+=" WHERE " + type + ".name LIKE '%" + value.replaceAll("'","''") + "%'";
			} else {
				sqlstmt+=" WHERE " + type + ".id = " + intVal;
			}

			sqlstmt+=" ORDER BY artist.name";
			return sqlstmt;
		}
	}
	
	private String searchDisc(String value)
	{
		int intVal = -1;
		try { intVal = Integer.parseInt(value,10);
		} catch (Throwable e) { new exhandle("Value is not a number, therefore must be a string: ", e, verbosityLevel); }
		String sqlstmt = "SELECT music.id, artist.name, album.name, format.name, music.disc FROM music JOIN artist ON music.artist = artist.id JOIN format ON music.format = format.id JOIN album ON music.album = album.id WHERE music.disc = " + intVal;
		return sqlstmt;
	}

	private  String searchAlias(String value)
	{
		int intVal = -1;
		try { intVal = Integer.parseInt(value,10);
		} catch (Throwable e) { new exhandle("Value is not a number, therefore must be a string: ", e, verbosityLevel); }
		//String sqlstmt = "SELECT music.id, artist.name, album.name, format.name, music.disc, alias.alias FROM music JOIN artist ON music.artist = artist.id JOIN format ON music.format = format.id JOIN album ON music.album = album.id JOIN alias ON artist.id = alias.master ";
		String sqlstmt = "SELECT music.id, artist.name, album.name, format.name, music.disc FROM music JOIN artist ON music.artist = artist.id JOIN format ON music.format = format.id JOIN album ON music.album = album.id ";
		
		if (intVal == -1) {
			//sqlstmt+="WHERE artist.name LIKE '%" + value.replaceAll("'","''") + "%' OR alias.alias LIKE '%" + value.replaceAll("'","''") + "%'";
			
			sqlstmt = "(" + sqlstmt;
			sqlstmt+="WHERE artist.name LIKE '%" + value.replaceAll("'","''") + "%') ";
			sqlstmt+="UNION ";
			sqlstmt+="(SELECT music.id, artist.name, album.name, format.name, music.disc FROM music ";
			sqlstmt+="JOIN artist ON music.artist = artist.id ";
			sqlstmt+="JOIN alias ON music.artist = alias.master ";
			sqlstmt+="JOIN format ON music.format = format.id ";
			sqlstmt+="JOIN album ON music.album = album.id ";
			sqlstmt+="WHERE alias.alias LIKE '%" + value.replaceAll("'","''") + "%')";
		} else {
			sqlstmt+="WHERE artist.id = " + intVal;
			sqlstmt+=" ORDER BY artist.name";
		}
		return sqlstmt;
	}

	/**
	 *   Show the whole music database; in SQL format.
	 */
	protected String showDB()
	{
		String sqlstmt = "SELECT music.id, artist.name, album.name, format.name, music.disc FROM music JOIN artist ON music.artist = artist.id JOIN format ON music.format = format.id JOIN album ON music.album = album.id ORDER BY artist.name DESC";
		return sqlstmt;
	}
	
	/**
	 *   Show all unarchived artists; in SQL format.
	 */
	protected String showUnarchived()
	{
		String sqlstmt = "SELECT music.id, artist.name, album.name, format.name, music.disc FROM music JOIN artist ON music.artist = artist.id JOIN format ON music.format = format.id JOIN album ON music.album = album.id WHERE format.name = 'MP3' AND disc = 0 ORDER BY artist.name DESC";
		return sqlstmt;
	}

	/**
	 *   Add to table "type" the value of "value"
	 */
	protected void addRecord(String type, String Value)
	{
		String sqlstmt = "INSERT INTO " + type + "(name) VALUES('" + Value.replaceAll("'","''") + "')";
		if (kerpowObjectManager.runDB.sqlRun(sqlstmt,null)) { System.out.println("Updated."); }
	}
	
	/**
	 *   Add a record to the "music" table.
	 */
	protected void addMusic(String artist, String album, String format, String disc)
	{
		int intVal = 0;
		try { intVal = Integer.parseInt(disc,10);
		} catch (Throwable e) { new exhandle("Value is not a number, therefore must be a string: ", e, verbosityLevel); }
		makeMusicCombo(checkAlbum(album),checkArtist(artist),checkFormat(format),intVal);
	}

	/**
	 *   Update a record in table "type" with a current value of 
	 *   "oldValue" to a new value of "newValue".
	 */
	protected void editRecord(String type, String oldValue, String newValue)
	{
		String sqlstmt = "";
		int theID = -1;

		if (type.equals("artist")) { theID = checkArtist(oldValue); } else
		if (type.equals("album")) { theID = checkAlbum(oldValue); }

		if (theID == -1) {
			System.out.println("Invalid record ID.");
		} else {
			sqlstmt = "UPDATE " + type + " SET name = '" + newValue.replaceAll("'","''") + "' WHERE id = " + theID;
		}

		if (kerpowObjectManager.runDB.sqlRun(sqlstmt,null)) { System.out.println("Updated."); }
	}

	/**
	 *   Update a record in the music table with the corresponding values.
	 */
	protected void editMusic(String id, String artist, String album, String format, String disc)
	{
		int intVal = -1;
		try { intVal = Integer.parseInt(id,10);
		} catch (Throwable e) { new exhandle("Music ID is not a number: ", e, verbosityLevel); }
		
		int mVal = 0;
		try { mVal = Integer.parseInt(disc,10);
		} catch (Throwable e) { new exhandle("disc value is not a number: ", e, verbosityLevel); }
	
		String sqlstmt = "UPDATE music SET artist = '" + checkArtist(artist) + "', album = '" + checkAlbum(album) + "', format = '" + checkFormat(format) + "', disc = " + disc + " WHERE id = " + intVal;
		kerpowObjectManager.runDB.sqlRun(sqlstmt,"Failed to make music combo: ");
	}

	protected int cleanMusic()
	{
		Vector delList = new Vector();
		int deleted = 0;
		
		try
		{
			// Get an artist list
			ResultSet rs = kerpowObjectManager.runDB.sqlExe("SELECT id FROM artist","Gone wrong, guv: ");
			while (!rs.isLast())
			{
				rs.next();
				delList.add(0,rs.getString(1));
			}
		}
		catch (Throwable e) { new exhandle("cleanMusic failed: ", e, verbosityLevel); }

				// For every artist ...
				for (int x = 0; x < delList.size(); x++)
				{		
					try {
						ResultSet rs = kerpowObjectManager.runDB.sqlExe("SELECT count(artist) FROM music WHERE artist = " + delList.get(x),"Really wrong, guv: ");
						rs.next();
						
						// Check it's use in music
						if (rs.getInt(1) == 0)
						{
							// Delete if not used.
							kerpowObjectManager.runDB.sqlRun("DELETE FROM artist WHERE id = " + delList.get(x),"Oh yeah, really really wrong: ");
							deleted++;
						}
						
						// Delete all aliases with that artist.
						kerpowObjectManager.runDB.sqlRun("DELETE FROM alias WHERE master = " + delList.get(x),"Really wrong, guv: ");
					}
					catch (Throwable e) { new exhandle("cleanMusic failed: ", e, verbosityLevel); }
				}
		return deleted;
	}
	
	/**
	 *   Checks to see if an artist (drawn from the music/ path) exists in the table "artist".  If no:  artist gets added.
	 *
	 *   ~~ Needs to be rewritten and parameterised with updateAlbum ~~
	 */
	protected void updateArtist(File dir, String[] epath)
	{
		if (dir.isDirectory()) {
			//System.out.println(dir);

			String[] children = dir.list();

			for (int i=0; i<children.length; i++) 
			{
				if (new File(dir, children[i]).isDirectory())
				{
					// Is this in the exclude list?
					boolean exclude = false;
					for (int x=0; x<epath.length; x++) {
						if ( (new File(dir, children[i]).toString()).equals(epath[x])) {
							exclude = true;
						}
					}
					
					if (!exclude)
					{
						// Does this artist already exist?
						if (checkArtist(children[i]) != -1)
						{
							// Yes.
							new exhandle("Artist " + children[i] + " exists.", null, verbosityLevel);
						} else {
							// No - so add it to the artist table
							kerpowObjectManager.runDB.sqlRun("insert into artist(name) values('" + children[i].replaceAll("'","''") + "')","Error adding 'album': ");
						}
						// Now check the albums ...
						updateAlbum(new File(dir, children[i]), epath, children[i]);
					}
				}
			}
        }
	}
	
	/**
	 *   Checks to see if an album (drawn from the music/ path) exists in the table "album".  If no:  album gets added.
	 *
	 *   ~~ Needs to be rewritten and parameterised with updateArtist ~~
	 */
	protected void updateAlbum(File dir, String[] epath, String parent)
	{
		if (dir.isDirectory()) {
			//System.out.println(dir);

			String[] children = dir.list();

			for (int i=0; i<children.length; i++) 
			{
				if (new File(dir, children[i]).isDirectory())
				{
					// Is this in the exclude list?
					boolean exclude = false;
					for (int x=0; x<epath.length; x++) {
						if ((new File(dir, children[i]).toString()).equals(epath[x])) { exclude = true; }
					}
					
					if (!exclude)
					{
						// Does this album already exist?
						if (checkAlbum(children[i]) != -1)
						{
							// Yes.
							new exhandle("Album " + children[i] + " exists.", null, verbosityLevel);
						} else {
							// No - so add it to the album table
							kerpowObjectManager.runDB.sqlRun("insert into album(name) values('" + children[i].replaceAll("'","''") + "')","Error adding 'album': ");
						}

						if (checkMusic(checkAlbum(children[i]), checkArtist(parent)) == -1)
						{
							makeMusicCombo(checkAlbum(children[i]), checkArtist(parent), 1, 0);
						}
					}
				}
			}
        }
	}
	
	/**
	 *   Adds a music combination (album/artist/format/disc) to the table "music".
	 */
	protected void makeMusicCombo(int album, int artist, int format, int disc)
	{
		kerpowObjectManager.runDB.sqlRun("insert into music(artist, album, format, disc) values(" + artist + ", " + album + ", " + format + ", " + disc + ")","Failed to make music combo: ");
	}

	/**
	 *   Checks if an artist exists in the table "artist".  Returns the artist ID or -1 if it doesn't exist.
	 */
	protected int checkArtist(String artistInfo)
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
		catch (Throwable e) { new exhandle("checkArtist failed: ", e, verbosityLevel); }
		
		if (result == -1)
		{
			try
			{
				ResultSet rs = kerpowObjectManager.runDB.sqlExe("SELECT master,alias FROM alias WHERE alias = '" + artist + "'"," ");
				rs.next();
				if (artist.equals(rs.getString(2))) { result = Integer.parseInt(rs.getString(1)); }
			} 
			catch (Throwable e) { new exhandle("checkArtist (alias) failed: ", e, verbosityLevel); }
		}
		return result;
	}
	
	/**
	 *   Checks if an album exists in the table "album".  Returns the album ID or -1 if it doesn't exist.
	 */
	protected int checkAlbum(String albumInfo)
	{
		String album = albumInfo.replaceAll("'","''");
		//System.out.println(album);
		int result = -1;
		try
		{
			ResultSet rs = kerpowObjectManager.runDB.sqlExe("SELECT * FROM album WHERE name = '" + album + "'"," ");
			rs.next();
			if (album.equals(rs.getString(2))) { result = Integer.parseInt(rs.getString(1)); } 
		} 
		catch (Throwable e) { new exhandle("checkAlbum failed: ", e, verbosityLevel); }
		
		return result;
	}

	/**
	 *   Checks a music combination (album/artist) from the table "music".
	 */
	protected int checkMusic(int album, int artist)
	{
		int result = -1;
		try
		{
			ResultSet rs = kerpowObjectManager.runDB.sqlExe("SELECT * FROM music WHERE artist = " + artist + " AND album = " + album," ");
			rs.next();
			result = Integer.parseInt(rs.getString(1));
		} 
		catch (Throwable e) { new exhandle("checkMusic failed: ", e, verbosityLevel); }
		
		return result;
	}
	
	protected int checkFormat(String formatInfo)
	{
		String format = formatInfo.replaceAll("'","''");
		//System.out.println(album);
		int result = -1;
		try
		{
			ResultSet rs = kerpowObjectManager.runDB.sqlExe("SELECT * FROM format WHERE name = '" + format + "'"," ");
			rs.next();
			if (format.equals(rs.getString(2))) { result = Integer.parseInt(rs.getString(1)); } 
		} 
		catch (Throwable e) { new exhandle("checkFormat failed: ", e, verbosityLevel); }
		
		return result;
	
	}
}
