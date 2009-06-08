// Database SQL imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// The rest...
import java.util.Properties;
import java.io.File;
import java.lang.Process;

public class music
{

	private int verbosityLevel = 0;
	private int[] columnWidths = new int[4];

	public music()
	{
		verbosityLevel = kerpowObjectManager.verbosityLevel;
		//System.out.println(verbosityLevel);
	}
	
	/**
	 *   Method call contents:
	 *
	 *		createTables()
	 *
	 *		User commands:
	 *			updateDB()
 	 *			showDB()
 	 *			searchDB()
	 *			editRecord()
	 *			editMusic()
	 *			addRecord()
	 *			addMusic()
	 *
	 *		Helper routines:
	 *			displayDB()	 
	 *			fixW()
	 *			checkArtist()
	 *			checkAlbum()
	 *			checkMusic()
	 *
	 *		updateArtist()
	 *		updateAlbum()
	 *		makeMusicCombo()	 
	 *
	 *		Mutators:
	 *			setColumns()	 
	 *			setVerbosity()
	 *
	 *		Interface extensions:
	 *			parseCommand()
	 *			buildCommands()
	 *			help()
	 */
	 
	 
	/**
	 *   Generates the music database tables, and populates any default fields.
	 */
	public void createTables()
	{
			// Make the tables:
			
		// Artist
		kerpowObjectManager.runDB.sqlMake("create table artist(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, name varchar(128))","'Artist' creation failed:");
		
		// Alias
		kerpowObjectManager.runDB.sqlMake("create table alias(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, name varchar(128), artistid int)","'Alias' creation failed:");

		// Album	
		kerpowObjectManager.runDB.sqlMake("create table album(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, name varchar(128))","'Album' creation failed:");
		
		// Music
		kerpowObjectManager.runDB.sqlMake("create table music(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, artist int, album int, format int, misc int)","'Music' creation failed:");
			
		// Format
		kerpowObjectManager.runDB.sqlMake("create table format(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, name varchar(128))","'Format' creation failed:");
		
		// Populate the format table
		kerpowObjectManager.runDB.sqlRun("insert into format(name) values('MP3')","\"format - MP3\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into format(name) values('CD')","\"format - CD\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into format(name) values('CD Original')","\"format - CD Original\" population failed:");
		
		// Release recordsets
		//kerpowObjectManager.runDB.dbCommit();
	}

	
	/**
	 *   Updates the music database from the details in the prefs file.
	 */
	public void updateDB(String[] mpath, String[] epath/*, String format*/)
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
	 *   The main display function that shows all records in music:
	 *
	 *   dbID	artist		album		format	misc
	 */
	private int showDB()
	{
		String sqlstmt = "SELECT music.id, artist.name, album.name, format.name, music.misc FROM music JOIN artist ON music.artist = artist.id JOIN format ON music.format = format.id JOIN album ON music.album = album.id ORDER BY artist.name";
		return displayDB(sqlstmt);
	}
	
	/**
	 *   A searchable display function that shows:
	 *
	 *   dbID	artist		album		format	misc
	 */
	private int searchDB(String type, String value)
	{
		int intVal = -1;
		try { intVal = Integer.parseInt(value,10);
		} catch (Throwable e) { new exhandle("Value is not a number, therefore must be a string: ", e, verbosityLevel); }
		String sqlstmt = "SELECT music.id, artist.name, album.name, format.name, music.misc FROM music JOIN artist ON music.artist = artist.id JOIN format ON music.format = format.id JOIN album ON music.album = album.id";
		if (intVal == -1) {
			sqlstmt+=" WHERE " + type + ".name LIKE '%" + value.replaceAll("'","''") + "%' ";
		} else {
			sqlstmt+=" WHERE " + type + ".id = " + intVal;
		}
		sqlstmt+=" ORDER BY artist.name";
		return displayDB(sqlstmt);
	}
	
/*
	private int displayDB(String sqlstmt)
	{
		int recordnum = 0;
		try {
			ResultSet rs = kerpowObjectManager.runDB.sqlExe(sqlstmt,null);
			while (!rs.isLast())
			{
				rs.next();
				System.out.println(fixW(rs.getString(1),columnWidths[0]) + fixW(rs.getString(2),columnWidths[1]) + " " + fixW(rs.getString(3),columnWidths[2]) + " " + fixW(rs.getString(4),columnWidths[3]) + fixW(rs.getString(5),columnWidths[4]));
				recordnum++;
			}
		} catch (Throwable e) { new exhandle("displayDB failed with: ", e, verbosityLevel); }
		
		return recordnum;
	}
*/	
	private int displayDB(String sqlstmt)
	{
		int recordnum = 0;
		try {
			ResultSet rs = kerpowObjectManager.runDB.sqlExe(sqlstmt,null);
			while (!rs.isLast())
			{
				rs.next();
				System.out.println(fixW(rs.getString(1),columnWidths[0]) + fixW(rs.getString(2),columnWidths[1]) + " " + fixW(rs.getString(3),columnWidths[2]) + " " + fixW(rs.getString(4),columnWidths[3]) + fixW(rs.getString(5),columnWidths[4]));
				recordnum++;
			}
		} catch (Throwable e) { new exhandle("displayDB failed with: ", e, verbosityLevel); }
		
		return recordnum;
	}
	
	private void addRecord(String type, String Value)
	{
		String sqlstmt = "INSERT INTO " + type + "(name) VALUES('" + Value.replaceAll("'","''") + "')";
		if (kerpowObjectManager.runDB.sqlRun(sqlstmt,null)) { System.out.println("Updated."); }
	}
	
	private void addMusic(String artist, String album, String format, String misc)
	{
		int intVal = 0;
		try { intVal = Integer.parseInt(misc,10);
		} catch (Throwable e) { new exhandle("Value is not a number, therefore must be a string: ", e, verbosityLevel); }
		makeMusicCombo(checkAlbum(album),checkArtist(artist),checkFormat(format),intVal);
	}

	private void editRecord(String type, String oldValue, String newValue)
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

	private void editMusic(String id, String artist, String album, String format, String misc)
	{
		int intVal = -1;
		try { intVal = Integer.parseInt(id,10);
		} catch (Throwable e) { new exhandle("Music ID is not a number: ", e, verbosityLevel); }
		
		int mVal = 0;
		try { mVal = Integer.parseInt(misc,10);
		} catch (Throwable e) { new exhandle("Misc value is not a number: ", e, verbosityLevel); }
	
		String sqlstmt = "UPDATE music SET artist = '" + checkArtist(artist) + "', album = '" + checkAlbum(album) + "', format = '" + checkFormat(format) + "', misc = " + misc + " WHERE id = " + intVal;
		kerpowObjectManager.runDB.sqlRun(sqlstmt,"Failed to make music combo: ");
	}

	/**
	 *   Formats the width of the columns based on the columnWidths portion of the prefs file.
	 */
	private String fixW(String msg, int size)
	{
		String result = "";
		if (msg.length() > size)
		{
			result = msg.substring(0,size-3) + "...";
		} else {
			int loopSize = size - (msg.length());
			result = msg;
			for (int x=0; x<loopSize; x++)
			{
				result+=" ";
			}
		}
		return result;
	}
	
	/**
	 *   Checks to see if an artist (drawn from the music/ path) exists in the table "artist".  If no:  artist gets added.
	 *
	 *   ~~ Needs to be rewritten and parameterised with updateAlbum ~~
	 */
	private void updateArtist(File dir, String[] epath)
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
	private void updateAlbum(File dir, String[] epath, String parent)
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
	 *   Adds a music combinattion (album/artist/format/misc) to the table "music".
	 */
	private void makeMusicCombo(int album, int artist, int format, int misc)
	{
		kerpowObjectManager.runDB.sqlRun("insert into music(artist, album, format, misc) values(" + artist + ", " + album + ", " + format + ", " + misc + ")","Failed to make music combo: ");
	}

	/**
	 *   Checks if an artist exists in the table "artist".  Returns the artist ID or -1 if it doesn't exist.
	 */
	private int checkArtist(String artistInfo)
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
				ResultSet rs = kerpowObjectManager.runDB.sqlExe("SELECT artistid,name FROM alias WHERE name = '" + artist + "'"," ");
				rs.next();
				if (artist.equals(rs.getString(2))) { result = Integer.parseInt(rs.getString(1)); }
			} 
			catch (Throwable e) { new exhandle("checkArtist failed: ", e, verbosityLevel); }
		}
		return result;
	}
	
	/**
	 *   Checks if an album exists in the table "album".  Returns the album ID or -1 if it doesn't exist.
	 */
	private int checkAlbum(String albumInfo)
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
	private int checkMusic(int album, int artist)
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
	
	private int checkFormat(String formatInfo)
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
	
	/**
	 *   Stores the column widths supplied in columnWidths.
	 */
	public void setColumns(int[] columns)
	{
		columnWidths = columns;
	}
	
	/**
	 *   Sets the verbosityLevel.  Deprecated in favour of public verbosityLevel in kerpowObjectManager.java
	 */
	public void setVerbosity(int level)
	{
		verbosityLevel = level;
	}
	
	public void parseCommand(String[] command)
	{
		if (command[0].equals("display") && command.length > 1) {
			if (command[1].equals("all")) {
				showDB(); 
			} else {
				if (command[1].equals("artist") | command[1].equals("album") | command[1].equals("format") | command[1].equals("alias")) {
					String[] mycomm = buildCommands(command);
					searchDB(mycomm[1],mycomm[2]);
				} else {
					System.out.println("Not a recognised command. Type \"help music\" to view available commands.");
				}
			}
		}
		else if (command[0].equals("update")) {
			updateDB(kerpowObjectManager.preferences.getMusicPath(),kerpowObjectManager.preferences.getExcludePath());
		}
		else if (command[0].equals("edit") && command.length > 1) {
			if (command[1].equals("artist") | command[1].equals("album") | command[1].equals("format") | command[1].equals("alias")) {
				String[] mycomm = buildCommands(command);
				editRecord(mycomm[1],mycomm[2],mycomm[3]);

				/*
				for (int i = 0; i < mycomm.length; i++)
				{
					System.out.println("\"" + mycomm[i] + "\"");
				}*/
			} else if (command[1].equals("music"))
			{
				String[] mycomm = buildCommands(command);
				editMusic(mycomm[2],mycomm[3],mycomm[4],mycomm[5],mycomm[6]);
			} else {
				System.out.println("Not a recognised command. Type \"help music\" to view available commands.");
				System.out.println("Expected 'artist', 'album', 'format' or 'music'.");
			}
		}
		else if (command[0].equals("add") && command.length > 1) {
			if (command[1].equals("artist") | command[1].equals("album") | command[1].equals("format") | command[1].equals("alias")) {
				String[] mycomm = buildCommands(command);
				addRecord(mycomm[1],mycomm[2]);
			} else if (command[1].equals("music")) {
				String[] mycomm = buildCommands(command);
				addMusic(mycomm[2],mycomm[3],mycomm[4],mycomm[5]);
			} else {
				System.out.println("Not a recognised command. Type \"help music\" to view available commands.");
				System.out.println("Expected 'artist', 'album', 'format' or 'music'.");
			}
		}
		else if (command[0].equals("exit")) {
			System.out.println("Farewell!");			
		} else {
			help();
		}
	}
	
	private String[] buildCommands(String[] command)
	{
		boolean longB = true;
		String sOut = "";
		String sTmp = "";
		String[] fixxed = new String[6];
		int fixCount = 0;
		
		//fixxed[fixCount] = "";

		for (int i = 0; i < command.length; i++) 
		{
			sOut = command[i];

			if (sOut.endsWith("\""))
			{
				sTmp+=sOut.substring(0,sOut.length()-1);
				longB = true;
				sOut = sTmp;
			} else {
				sTmp+=sOut + " ";
			}

			if (sOut.startsWith("\""))
			{
				sTmp = sOut.substring(1) + " ";
				longB = false;
			}

			if (longB)
			{
				//System.out.println("\"" + sOut + "\"");
				fixxed[fixCount] = sOut;
				fixCount++;
				
			}
			//System.out.println(sOut);
		}
		
		return fixxed;
	}

	
	public void help()
	{
		String msg="";
		
		msg+="Music archive commands:\n\n";
		
		msg+="  display	all\n";
		msg+="		artist <artist name | artistID>\n";
		msg+="		album <album name | albumID>\n";
		msg+="		format <format type | formatID>\n";
		msg+="		alias <alias name | aliasID>\n\n";
		msg+="  update	updates the archive from your local music directories.\n\n";
		msg+="  add		music <artist> <album> <format> <misc>\n\n";
		msg+="  home		return home\n\n";

		System.out.println(msg);
	}

}
