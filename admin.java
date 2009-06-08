// Database SQL imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;

public class admin
{
	private int verbosityLevel = 0;
	private int[] columnWidths = new int[4];

	public admin()
	{
		verbosityLevel = kerpow.verbosityLevel;
		columnWidths = kerpow.preferences.getColumns();
	}
	
	private void runsql(String sqlstmt)
	{
		System.out.println("Running: \n" + sqlstmt);
		if (sqlstmt.startsWith("UPDATE") | sqlstmt.startsWith("INSERT INTO"))
		{
			kerpow.runDB.sqlRun(sqlstmt,"runsql failed on UPDATE/INSERT: ");
		} else if (sqlstmt.startsWith("SELECT")) {
			String[] clString = sqlstmt.split("\\s");
			String table = "";
			
			for (int x = 0; x < clString.length; x++)
			{
				if (clString[x].equals("FROM"))
				{ 
					table = clString[x+1]; 
					x+=clString.length;
				}
			}
			
			try {
			ResultSet rs = kerpow.runDB.sqlExe(sqlstmt,"runsql failed on SELECT: ");
			displayDB(rs,1,getColumnNumber(table)+1);
			} catch (Throwable e) {}
		} else {
			
		}
	}
	
	private int list(String table, String value)
	{
		boolean selAll = false;
		if (value.equals("all")) { selAll = true; }
		
		int result = -1;
		String sqlstmt = "";
		
		if (table.equals("tables"))
		{
			DatabaseMetaData thisdb = kerpow.runDB.getMetaData();
			
			if (selAll){
				try {
				ResultSet rs = thisdb.getTables("SYS","APP",null,null);
				result = displayDB(rs,3,4);
				} catch (Throwable e) {System.out.println(e);}
			} else {
				try {
				ResultSet rs = thisdb.getColumns("SYS","APP",value,null);
				result = displayDB(rs,4,5);
				} catch (Throwable e) {System.out.println(e);}
			}
			
		} else {

			sqlstmt = "SELECT * FROM " + table;
			
			if (!selAll)
			{
				int valN = -1;
				try { valN = Integer.parseInt(value,10);
				} catch (Throwable e) { new exhandle("add(): Value is not a number, must be a string: ", e, verbosityLevel); }

				sqlstmt+=" WHERE ";
				
				// New value is a string
				if (valN == -1){
					sqlstmt+="'" + value + "'";
				}else{
					sqlstmt+=" id = " + valN;
				}
			}
			result = displayDB(sqlstmt,1,getColumnNumber(table)+1);
		}
		return result;
	}
	
	private boolean add(String table, String column, String value)
	{
		int valN = -1;
		try { valN = Integer.parseInt(value,10);
		} catch (Throwable e) { new exhandle("add(): Value is not a number, must be a string: ", e, verbosityLevel); }
		
		
		String sqlstmt = "INSERT INTO " + table + "(" + column + ") VALUES(";
		
		// New value is a string
		if (valN == -1){
			sqlstmt+="'" + value + "'";
		}else{
			sqlstmt+=valN;
		}
		return kerpow.runDB.sqlRun(sqlstmt,"Failed to add records: ");
	}
	
	private boolean edit(String table, String newvalue, String column, String oldvalue)
	{
		int intVal = -1;
		try { intVal = Integer.parseInt(oldvalue,10);
		} catch (Throwable e) { new exhandle("edit(): Value is not a number, must be a string: ", e, verbosityLevel); }
		
		int valN = -1;
		try { valN = Integer.parseInt(newvalue,10);
		} catch (Throwable e) { new exhandle("edit(): Value is not a number, must be a string: ", e, verbosityLevel); }
		
		
		String sqlstmt = "UPDATE " + table + " SET " + column + " = ";
		
		// New value is a string
		if (valN == -1){
			sqlstmt+="'" + newvalue + "'";
		}else{
			sqlstmt+=valN;
		}
		
		sqlstmt+=" WHERE ";
		
		// Old value is a string
		if (intVal == -1){
			sqlstmt+=column + " = '" + oldvalue + "'";
		} else {
			sqlstmt+="id = " + intVal;
		}
		return kerpow.runDB.sqlRun(sqlstmt,"Failed to edit records: ");
	}
	
	private boolean delete(String table, String column, String value)
	{
		int intVal = -1;
		try { intVal = Integer.parseInt(column,10);
		} catch (Throwable e) { new exhandle("delete(): Value is not a number, must be a string: ", e, verbosityLevel); }
		
		String sqlstmt = "DELETE * FROM " + table + " WHERE ";
		if (intVal == -1)
		{
			sqlstmt+=column + " = '" + value + "'";
		} else {
			sqlstmt+="id = " + intVal;
		}
		return kerpow.runDB.sqlRun(sqlstmt,"Failed to delete records: ");
	}

	/**
	 *   Formats the width of the columns based on the columnWidths portion of the prefs file.
	 */
	private String fixW(String msg, int size)
	{
		String result = "";
		if (msg.length() > size)
		{
			result = msg.substring(0,size-3) + "... ";
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

	private int getColumnNumber(String tablename)
	{
		DatabaseMetaData thisdb = kerpow.runDB.getMetaData();
		int result = 0;
		try {
			ResultSet rs = thisdb.getColumns("SYS","APP",tablename,null);
			while (rs.next())
			{
				result++;
			}
		} catch (Throwable e) { }
		
		return result;
	}
	
	private int displayDB(String sqlstmt, int cstart, int cend)
	{
		int recordnum = 0;
		try {
			ResultSet rs = kerpow.runDB.sqlExe(sqlstmt,null);
			while (rs.next())
			{
				//System.out.println(fixW(rs.getString(1),columnWidths[0]) + fixW(rs.getString(2),columnWidths[1]) + fixW(rs.getString(3),columnWidths[2]) + fixW(rs.getString(4),columnWidths[3]) + fixW(rs.getString(5),columnWidths[4]));
				for (int x = cstart; x < cend; x++)
				{
					System.out.print(fixW(rs.getString(x),12));
				}
				System.out.print("\n");
				recordnum++;
			}
		} catch (Throwable e) { new exhandle("displayDB failed with: ", e, verbosityLevel); }
		
		return recordnum;
	}
	
	private int displayDB(ResultSet rs, int cstart, int cend)
	{
		int recordnum = 0;
		try {
			while (rs.next())
			{
				//System.out.println(fixW(rs.getString(1),columnWidths[0]) + fixW(rs.getString(2),columnWidths[1]) + " " + fixW(rs.getString(3),columnWidths[2]) + " " + fixW(rs.getString(4),columnWidths[3]) + fixW(rs.getString(5),columnWidths[4]));
				for (int x = cstart; x < cend; x++)
				{
					System.out.print(fixW(rs.getString(x),12));
				}
				System.out.print("\n");
				recordnum++;
			}
		} catch (Throwable e) { new exhandle("displayDB failed with: ", e, verbosityLevel); }
		
		return recordnum;	
	}
	
	public void parseCommand(String[] commands)
	{
		String[] command = buildCommands(commands);
		
		if (command[0].equals("list") && command.length > 1) {
			list(command[1],command[2]);
		} else if (command[0].equals("add") && command.length > 1) {
			add(command[1],command[2],command[3]);
		} else if (command[0].equals("edit") && command.length > 1) {
			edit(command[1],command[2],command[3],command[4]);
		} else if (command[0].equals("delete") && command.length > 1) {
			delete(command[1],command[2],command[3]);
		} else if (command[0].equals("runsql") && command.length > 1) {
			runsql(command[1]);
		} else if (command[0].equals("help")) {
			help();
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
		msg+="Archive administrative commands:\n\n";
		msg+="  list		<table-name> [ all | <ID> ]\n";
		msg+="  list		tables [ all | <value> ]\n\n";
		msg+="  add		<table> <column> <value>\n";
		msg+="  edit		<table> <new value> <column> [ <old value> | <record id> ]\n";
		msg+="  delete	<table> [ <column> <value> | <record id> ]\n\n";
		msg+="  runsql	<SQL statement>\n\n";

		System.out.println(msg);
	}
}
