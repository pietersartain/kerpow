package plugins.video;

import com.kaear.cli.*;
import com.kaear.common.*;
import com.kaear.gui.*;

// Database SQL imports
import java.sql.ResultSet;

// The rest...
import java.io.File;
import java.util.Vector;
import java.util.Date;

public class videoCommands implements Runnable
{

	private int verbosityLevel = 0;
	private String cmd;
	private Vector args;

	public videoCommands(String cmd, Vector args)
	{
		verbosityLevel = videoMain.verbosityLevel;
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
		if (cmd.equals("findFilm")) {
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
             public void run() {
				new videoFilmsGui().updateTable(findFilm((String)args.get(0),(String)args.get(1)));
             }});
		
		kerpowgui.updateStatusBar("Search completed.");				


		} else if (cmd.equals("findSeries")) {
		}
	}
	
	/**
	 *   Generates the video database tables, and populates any default fields.
	 */
	public void createTables()
	{
		// Make the tables:
			
		// Video
		kerpowObjectManager.runDB.sqlMake("CREATE TABLE video(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, format int, type int, location varchar(128), disks int, type_id int, quality int, classification int)","'Video' creation failed:");
		
		// Format
		kerpowObjectManager.runDB.sqlMake("CREATE TABLE videoformat(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, name varchar(128))","'Format' creation failed:");

		// Type	
		kerpowObjectManager.runDB.sqlMake("CREATE TABLE type(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, name varchar(128))","'Type' creation failed:");
		
		// Series
		kerpowObjectManager.runDB.sqlMake("CREATE TABLE series(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, name varchar(128), episodes int, season int)","'Series' creation failed:");
			
		// Films
		kerpowObjectManager.runDB.sqlMake("CREATE TABLE films(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, name varchar(128))","'Films' creation failed:");
		
		// Quality
		kerpowObjectManager.runDB.sqlMake("CREATE TABLE quality(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, name varchar(128))","'Quality' creation failed:");

		// Classification
		kerpowObjectManager.runDB.sqlMake("CREATE TABLE classification(id INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key, name varchar(128))","'Classification' creation failed:");

		// Populate the format table
		kerpowObjectManager.runDB.sqlRun("insert into videoformat(name) values('DVD Original')","\"format - DVD Original\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into videoformat(name) values('DVD')","\"format - DVD\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into videoformat(name) values('VCD')","\"format - DVD Original\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into videoformat(name) values('SVCD')","\"format - DVD Original\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into videoformat(name) values('Media File')","\"format - Media File\" population failed:");
		
		// Populate the quality table
		kerpowObjectManager.runDB.sqlRun("insert into quality(name) values('Best')","\"quality - Best\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into quality(name) values('Good')","\"quality - Good\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into quality(name) values('Average')","\"quality - Average\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into quality(name) values('Poor')","\"quality - Poor\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into quality(name) values('Worst')","\"quality - Worst\" population failed:");

		// Populate the type table
		kerpowObjectManager.runDB.sqlRun("insert into type(name) values('series')","\"type - series\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into type(name) values('films')","\"type - films\" population failed:");
		
		// Populate the classification table
		kerpowObjectManager.runDB.sqlRun("insert into classification(name) values('80s')","\"type - films\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into classification(name) values('Action/Adventure')","\"type - films\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into classification(name) values('Anime')","\"type - series\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into classification(name) values('Animated')","\"type - films\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into classification(name) values('Comedy')","\"type - films\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into classification(name) values('Classics')","\"type - films\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into classification(name) values('Drama')","\"type - films\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into classification(name) values('Thriller')","\"type - films\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into classification(name) values('Romantic comedy')","\"type - films\" population failed:");
		kerpowObjectManager.runDB.sqlRun("insert into classification(name) values('SciFi')","\"type - films\" population failed:");
				
		// Make sure we don't do this every time
		try { new File(".status/run.video").createNewFile(); }
		catch (Throwable e) { new exhandle("Cannot create file run.video! ",e,2); }
	}

	public boolean checkRun()
	{
			File runOnce = new File(".status/run.video");
			return runOnce.exists();	
	}
	
	protected String showSeries()
	{
		String sqlstmt = "SELECT video.id, series.name, series.season, series.episodes, video.disks, videoformat.name, quality.name, video.location FROM video JOIN videoformat ON video.format = videoformat.id JOIN series ON video.type_id = series.id JOIN quality ON video.quality = quality.id JOIN classification ON video.classification = classification.id ORDER BY series.name DESC";
		return sqlstmt;
	}
	
	protected String showFilms()
	{
		String sqlstmt = "SELECT video.id, films.name, video.disks, videoformat.name, quality.name, video.location, classification.name FROM video JOIN videoformat ON video.format = videoformat.id JOIN films ON video.type_id = films.id JOIN quality ON video.quality = quality.id JOIN classification ON video.classification = classification.id ORDER BY films.name DESC"; 
		return sqlstmt;
	}
	
	protected boolean addFilm(String name, int disks, String format, String quality, String location, String classification)
	{
		try {
			if (getID(name,"films","name") == -1)
				kerpowObjectManager.runDB.sqlRun("INSERT INTO films(name) VALUES('" + name + "')"," ");
			
			String sqlstmt = "INSERT INTO video(format, type, location, disks, quality, type_id, classification) ";
			sqlstmt+= "VALUES(" + getID(format,"videoformat","name") + ", " + getID("films","type","name") + ", '" + location + "', " + disks + ", " + getID(quality,"quality","name") + ", " + 		getID(name,"films","name") + ", " + getID(classification,"classification","name") + ")";
		//System.out.println(sqlstmt);

			kerpowObjectManager.runDB.sqlRun(sqlstmt," ");
			return true;
		} catch (Throwable e) { 
			new exhandle("addFilm failed: ", e, verbosityLevel); 
			return false;
		}
	}
	
	protected boolean addSeries()
	{
		return false;
	}
	
	protected String findFilm(String name, String field)
	{
		String sqlstmt = "SELECT video.id, films.name, video.disks, videoformat.name, quality.name, video.location, classification.name FROM video JOIN videoformat ON video.format = videoformat.id JOIN films ON video.type_id = films.id JOIN quality ON video.quality = quality.id JOIN classification ON video.classification = classification.id WHERE ";
		
		if (field.equals("Location")) { sqlstmt+= "video.location LIKE '%" + name + "%'"; }
		else
		if (field.equals("Name")) { sqlstmt+= "films.name LIKE '%" + name + "%'"; }
		else
		{
			int id = getID(name,"name",field);
			sqlstmt+= field + ".id = " + id;
		}
		
		sqlstmt+= " ORDER BY films.name DESC";
		return sqlstmt;
	}
	
	protected int getID(String name, String table, String field)
	{
		String sqlstmt = "SELECT id, " + field + " FROM " + table + " WHERE " + field + " = '" + name + "'";
		//System.out.println(sqlstmt);
		
		int result = -1;
		try
		{
			ResultSet rs = kerpowObjectManager.runDB.sqlExe(sqlstmt," ");
			rs.next();
			if (name.equals(rs.getString(2))) { result = Integer.parseInt(rs.getString(1)); }
		} 
		catch (Throwable e) { new exhandle("getID failed: ", e, verbosityLevel); }
		return result;
	}
	
	
}
