package plugins.minidisk;

import com.kaear.interfaces.*;
import com.kaear.gui.*;
import com.kaear.common.*;

// Database SQL imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// The rest...
import java.util.*;
import java.io.File;
import java.lang.Process;

public class minidiskList implements dataList
{

	private int verbosityLevel = 0;
	private String sqlstmt;
	
	public minidiskList(String sqlstmt)
	{
		this.sqlstmt = sqlstmt;
	}
	
	public Vector makeList()
	{

		//dbase runDB = new dbase();
		Vector data = new Vector();

		try {
			ResultSet rs = kerpowObjectManager.runDB.sqlExe(sqlstmt,null);
			while (!rs.isLast())
			{
				rs.next();
				
				String[] thisRow = new String[getColumnHeaders().length];
				for (int i = 0; i < getColumnHeaders().length; i++) {
					thisRow[i] = rs.getString(i+1);
				}
				
				data.add(0,thisRow);
			}
		} catch (Throwable e) { new exhandle("videoFilmsList.makeList() failed with: ", e); }
		
		return data;
	}
	
	public String[] getColumnHeaders()
	{
		return new String[] {"ID","Name","Number","Contents"};
	}
}
