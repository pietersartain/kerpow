package plugins.music.alias;

import com.kaear.cli.*;
import com.kaear.common.*;
import com.kaear.gui.*;

// The rest...
import java.util.Properties;
import java.io.File;
import java.lang.Process;
import java.util.Vector;

// Database SQL imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class aliasText implements cliPlugin
{

	private int verbosityLevel = 0;
	private int[] columnWidths = new int[4];

	public aliasText()
	{
		verbosityLevel = musicMain.verbosityLevel;
		columnWidths = musicMain.mp.getColumns();
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
	 *   The display function that shows all records according to sqlstmt:
	 *
	 *   dbID	artist		album		format	misc
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
	
	
	/**
	 *   Stores the column widths supplied in columnWidths.
	 */
	public void setColumns(int[] columns)
	{
		columnWidths = columns;
	}

	/**
	 *   Parses a text command and executes serially. (non-interactive)
	 */
	public void parseCliCommand(String[] command)
	{
		if (command[0].equals("display") && command.length > 1) {
			if (command[1].equals("all")) {
						displayDB(new musicCommands(null,null).showDB());
			} else {
				if (command[1].equals("artist") | command[1].equals("album") | command[1].equals("format") | command[1].equals("alias")) {
					String[] mycomm = buildCommands(command);
					new musicCommands(null,null).searchDB(mycomm[1],mycomm[2]);
				} else {
					System.out.println("Not a recognised command. Type \"help music\" to view available commands.");
				}
			}
		}
		else if (command[0].equals("update")) {
		new musicCommands(null,null).updateDB(musicMain.mp.getMusicPath(),musicMain.mp.getExcludePath());
					}
		else if (command[0].equals("edit") && command.length > 1) {
			if (command[1].equals("artist") | command[1].equals("album") | command[1].equals("format") | command[1].equals("alias")) {
				String[] mycomm = buildCommands(command);
				new musicCommands(null,null).editRecord(mycomm[1],mycomm[2],mycomm[3]);

			} else if (command[1].equals("music"))
			{
				String[] mycomm = buildCommands(command);
				new musicCommands(null,null).editMusic(mycomm[2],mycomm[3],mycomm[4],mycomm[5],mycomm[6]);
			} else {
				System.out.println("Not a recognised command. Type \"help music\" to view available commands.");
				System.out.println("Expected 'artist', 'album', 'format' or 'music'.");
			}
		}
		else if (command[0].equals("add") && command.length > 1) {
			if (command[1].equals("artist") | command[1].equals("album") | command[1].equals("format") | command[1].equals("alias")) {
				String[] mycomm = buildCommands(command);
				new musicCommands(null,null).addRecord(mycomm[1],mycomm[2]);
			} else if (command[1].equals("music")) {
				String[] mycomm = buildCommands(command);
				new musicCommands(null,null).addMusic(mycomm[2],mycomm[3],mycomm[4],mycomm[5]);
			} else {
				System.out.println("Not a recognised command. Type \"help music\" to view available commands.");
				System.out.println("Expected 'artist', 'album', 'format' or 'music'.");
			}
		
		}
		else if (command[0].equals("exit")) {
			//System.out.println("Farewell!");			
		}
		 else {
			help();
		}
	}

	/**
	 *   Parses a text command and executes in a thread-safe manner. (interactive/gui)
	 */
	public void parseCommand(String[] command)
	{
		if (command[0].equals("display") && command.length > 1) {
			if (command[1].equals("all")) {
			
						displayDB(new musicCommands(null,null).showDB());
						//musicMain.Comm.showDB(); 
			} else {
				if (command[1].equals("artist") | command[1].equals("album") | command[1].equals("format") | command[1].equals("alias")) {
					String[] mycomm = buildCommands(command);
					//musicMain.Comm.searchDB(mycomm[1],mycomm[2]);
					Vector args = new Vector();
					args.add(0,mycomm[1]);
					args.add(1,mycomm[2]);
					new musicCommands("searchDB",args);
				} else {
					System.out.println("Not a recognised command. Type \"help music\" to view available commands.");
				}
			}
		}
		else if (command[0].equals("update")) {
//musicMain.Comm.updateDB(kerpowObjectManager.preferences.getMusicPath(),kerpowObjectManager.preferences.getExcludePath());
		Vector args = new Vector();
		args.add(0,musicMain.mp.getMusicPath());
		args.add(1,musicMain.mp.getExcludePath());
		new musicCommands("updateDB",args);
					}
		else if (command[0].equals("edit") && command.length > 1) {
			if (command[1].equals("artist") | command[1].equals("album") | command[1].equals("format") | command[1].equals("alias")) {
				String[] mycomm = buildCommands(command);
				//musicMain.Comm.editRecord(mycomm[1],mycomm[2],mycomm[3]);
				Vector args = new Vector();
				args.add(0,mycomm[1]);
				args.add(1,mycomm[2]);
				args.add(2,mycomm[3]);				
				new musicCommands("editRecord",args);

			} else if (command[1].equals("music"))
			{
				String[] mycomm = buildCommands(command);
				//musicMain.Comm.editMusic(mycomm[2],mycomm[3],mycomm[4],mycomm[5],mycomm[6]);
				Vector args = new Vector();
				args.add(0,mycomm[2]);
				args.add(1,mycomm[3]);
				args.add(2,mycomm[4]);				
				args.add(3,mycomm[5]);				
				args.add(4,mycomm[6]);				
				new musicCommands("editMusic",args);
			} else {
				System.out.println("Not a recognised command. Type \"help music\" to view available commands.");
				System.out.println("Expected 'artist', 'album', 'format' or 'music'.");
			}
		}
		else if (command[0].equals("add") && command.length > 1) {
			if (command[1].equals("artist") | command[1].equals("album") | command[1].equals("format") | command[1].equals("alias")) {
				String[] mycomm = buildCommands(command);
				//musicMain.Comm.addRecord(mycomm[1],mycomm[2]);
				Vector args = new Vector();
				args.add(0,mycomm[1]);
				args.add(1,mycomm[2]);
				new musicCommands("addRecord",args);
			} else if (command[1].equals("music")) {
				String[] mycomm = buildCommands(command);
				//musicMain.Comm.addMusic(mycomm[2],mycomm[3],mycomm[4],mycomm[5]);
				Vector args = new Vector();
				args.add(0,mycomm[2]);
				args.add(1,mycomm[3]);
				args.add(2,mycomm[4]);				
				args.add(3,mycomm[5]);				
				new musicCommands("addMusic",args);
			} else {
				System.out.println("Not a recognised command. Type \"help music\" to view available commands.");
				System.out.println("Expected 'artist', 'album', 'format' or 'music'.");
			}
		
		}
		else if (command[0].equals("exit")) {
			//System.out.println("Farewell!");			
		}
		 else {
			help();
		}
	}

	/**
	 *   Strips the commands and reconstructs multipart lines.
	 */
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

	/**
	 *   Prints out the help message for the CLI.
	 */
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
