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
		verbosityLevel = kerpow.verbosityLevel;
	}
	
	public void createTables()
	{
			// Make the tables:
			
		// Artist
		kerpow.runDB.sqlMake("create table artist(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, name varchar(64))","'Artist' creation failed:");

		// Album	
		kerpow.runDB.sqlMake("create table album(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, name varchar(64))","'Album' creation failed:");
		
		// Music
		kerpow.runDB.sqlMake("create table music(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, artist int, album int, format int, misc int)","'Music' creation failed:");
			
		// Format
		kerpow.runDB.sqlMake("create table format(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, name varchar(64))","'Format' creation failed:");
		
		// Populate the format table
		kerpow.runDB.sqlRun("insert into format(name) values('MP3')","\"format\" population failed:");
		kerpow.runDB.sqlRun("insert into format(name) values('CD')","\"format\" population failed:");
		kerpow.runDB.sqlRun("insert into format(name) values('CD Original')","\"format\" population failed:");
		
		// Release recordsets
		//try { screate.close(); } catch (Throwable e) { new exhandle("Unable to close statement:", e, verbosityLevel); }
		kerpow.runDB.dbCommit();
	}
	
	public void updateDB(String[] mpath, String[] epath)
	{
		String[] musicpath = mpath;
		for (int x = 0; x < musicpath.length; x++)
		{
			File dir = new File(musicpath[x]);
			updateArtist(dir, epath);
		}
	}
	
	public void showDB()
	{
		try {
		
		String sqlstmt = "SELECT music.id, artist.name, album.name, format.name, music.misc FROM music JOIN artist ON music.artist = artist.id JOIN format ON music.format = format.id JOIN album ON music.album = album.id ORDER BY artist.name";
		
			//ResultSet rs = s.executeQuery(sqlstmt);
			ResultSet rs = kerpow.runDB.sqlExe(sqlstmt,null);
			//rs.next();
			
			while (!rs.isLast())
			{
				rs.next();
				System.out.println(fixW(rs.getString(1),columnWidths[0]) + fixW(rs.getString(2),columnWidths[1]) + " " + fixW(rs.getString(3),columnWidths[2]) + " " + fixW(rs.getString(4),columnWidths[3]) + fixW(rs.getString(5),columnWidths[4]));
			}
		} catch (Throwable e) { new exhandle("showDB failed with: ", e, verbosityLevel); }
			
			

	}
	
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
						
						kerpow.runDB.sqlRun("insert into artist(name) values('" + children[i].replaceAll("'","''") + "')","Error adding 'album': ");
						/*
							try {
							// No - so add it to the artist table
							s.execute("insert into artist(name) values('" + children[i].replaceAll("'","''") + "')");
							} catch (Throwable e) { new exhandle("Error adding 'album': ", e, verbosityLevel); }
						*/
						}

						// Now check the albums ...
						updateAlbum(new File(dir, children[i]), epath, children[i]);
					}
				}
			}
        	}
	}
	
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
						
						kerpow.runDB.sqlRun("insert into album(name) values('" + children[i].replaceAll("'","''") + "')","Error adding 'album': ");
						/*
							try {
							// No - so add it to the album table
							s.execute("insert into album(name) values('" + children[i].replaceAll("'","''") + "')");
							} catch (Throwable e) { new exhandle("Error adding 'album': ", e, verbosityLevel); }
						*/
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
	
	private void makeMusicCombo(int album, int artist, int format, int misc)
	{
		kerpow.runDB.sqlRun("insert into music(artist, album, format, misc) values(" + artist + ", " + album + ", " + format + ", " + misc + ")","Failed to make music combo: ");
	/*
		try {
			s.execute("insert into music(artist, album, format, misc) values(" + artist + ", " + album + ", " + format + ", " + misc + ")");
		} catch (Throwable e) { new exhandle("Failed to make music combo: ", e, verbosityLevel); }
	*/
	}

	private int checkArtist(String artistInfo)
	{
		String artist = artistInfo.replaceAll("'","''");
		//System.out.println(artist);
		int result = -1;
		try
		{
			//ResultSet rs = s.executeQuery("SELECT * FROM artist WHERE name = '" + artist + "'");
			ResultSet rs = kerpow.runDB.sqlExe("SELECT * FROM artist WHERE name = '" + artist + "'"," ");
			rs.next();
			if (artist.equals(rs.getString(2))) { result = Integer.parseInt(rs.getString(1)); }
		} 
		catch (Throwable e) { new exhandle("checkArtist failed: ", e, verbosityLevel); }
		
		return result;
	}
	
	private int checkAlbum(String albumInfo)
	{
		String album = albumInfo.replaceAll("'","''");
		//System.out.println(album);
		int result = -1;
		try
		{
			//ResultSet rs = s.executeQuery("SELECT * FROM album WHERE name = '" + album + "'");
			ResultSet rs = kerpow.runDB.sqlExe("SELECT * FROM album WHERE name = '" + album + "'"," ");
			rs.next();
			if (album.equals(rs.getString(2))) { result = Integer.parseInt(rs.getString(1)); } 
		} 
		catch (Throwable e) { new exhandle("checkAlbum failed: ", e, verbosityLevel); }
		
		return result;
	}
	
	private int checkMusic(int album, int artist)
	{
		int result = -1;
		try
		{
			//ResultSet rs = s.executeQuery("SELECT * FROM music WHERE artist = " + artist + " AND album = " + album);
			ResultSet rs = kerpow.runDB.sqlExe("SELECT * FROM music WHERE artist = " + artist + " AND album = " + album,"null");
			rs.next();
			result = Integer.parseInt(rs.getString(1));
		} 
		catch (Throwable e) { new exhandle("checkMusic failed: ", e, verbosityLevel); }
		
		return result;
	}
	
	public void setColumns(int[] columns)
	{
		columnWidths = columns;
	}
	
		public void setVerbosity(int level)
	{
		verbosityLevel = level;
	}

}
