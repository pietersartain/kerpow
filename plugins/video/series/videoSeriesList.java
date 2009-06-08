package plugins.video.series;

import com.kaear.gui.*;
import com.kaear.common.*;
import com.kaear.interfaces.*;

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

public class videoSeriesList implements dataList
{
	private String sqlstmt;
	
	public videoSeriesList(String sqlstmt)
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
		} catch (Throwable e) { new exhandle("videoSeriesList.makeList() failed with: ", e); }
		
		return data;
	}
	
	public String[] getColumnHeaders()
	{
		return new String[] {"ID","Name","Season","Episodes","Disks","Format","Quality","Location","Classification"};
	}
}
